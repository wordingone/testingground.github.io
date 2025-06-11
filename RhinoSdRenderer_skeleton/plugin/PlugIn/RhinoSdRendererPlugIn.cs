using Rhino;
using Rhino.DocObjects;
using Rhino.PlugIns;
using Rhino.Display;
using System;
using System.Diagnostics;
using System.Drawing;
using System.Runtime.InteropServices;
using Python.Runtime;

namespace RhinoSdRenderer
{
    /// <summary>Main entry point for the Stable‑Diffusion render plug‑in.</summary>
    public sealed class RhinoSdRendererPlugIn : RenderPlugIn
    {
        public RhinoSdRendererPlugIn() : base("RhinoSdRenderer", "YourCompany", PlugInType.Render)
        { }

        /// <summary>Initialise Python bridge on load.</summary>
        protected override LoadReturnCode OnLoad(ref string errorMessage)
        {
            try
            {
                // Initialise pythonnet runtime & append python path
                Runtime.PythonDLL = "python310.dll";         // TODO: make configurable
                PythonEngine.Initialize();
                using (Py.GIL())
                {
                    dynamic sys = Py.Import("sys");
                    sys.path.append("../../python");
                }
                return LoadReturnCode.Success;
            }
            catch (Exception ex)
            {
                errorMessage = ex.ToString();
                return LoadReturnCode.ErrorNoDialog;
            }
        }

        protected override void OnShutdown()
        {
            if (PythonEngine.IsInitialized)
                PythonEngine.Shutdown();
            base.OnShutdown();
        }

        /// <summary>Handle the render command.</summary>
        protected override Rhino.Commands.Result Render(RhinoDoc doc, Rhino.Commands.RunMode mode, bool fastPreview)
        {
            Stopwatch sw = Stopwatch.StartNew();

            // 1) Capture viewport to bitmap
            var view = RhinoApp.ActiveDoc.Views.ActiveView;
            var bmp  = view.CaptureToBitmap(new Size(1024, 1024), DisplayModeDescription.Shaded);

            // 2) Convert to bytes and hand over to Python
            byte[] pngBytes;
            using (var ms = new System.IO.MemoryStream())
            {
                bmp.Save(ms, System.Drawing.Imaging.ImageFormat.Png);
                pngBytes = ms.ToArray();
            }

            Bitmap resultBmp = null;
            using (Py.GIL())
            {
                dynamic bridge = Py.Import("main");  // python/main.py
                var pyBytes    = new PyBytes(pngBytes);
                dynamic pyImg  = bridge.render_image(pyBytes, "brutalist atrium"); // placeholder prompt
                resultBmp      = (Bitmap)pyImg.AsManagedObject(typeof(Bitmap));
            }

            // 3) Show in Rhino Render Window
            var rw = RenderWindow.FromActiveView();
            rw.Clear();
            rw.DrawBitmap(resultBmp, 0, 0);

            RhinoApp.WriteLine($"SD render finished in {sw.ElapsedMilliseconds} ms");
            return Rhino.Commands.Result.Success;
        }
    }
}
