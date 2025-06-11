# Local setup

1. Install **Visual Studio 2022 Community** with “.NET desktop” + “Desktop development with C++” workloads.
2. Install **.NET 7 SDK** (x64).
3. Install **CUDA 11.8** toolkit and verify `nvidia-smi` shows your GPU.
4. Install **Python 3.10 ×64** and create a virtual environment:
   ```ps1
   python -m venv venv
   venv\Scripts\activate
   pip install -r python\requirements.txt
   ```
5. Download model weights:
   ```ps1
   python scripts\download_models.py
   ```
6. Build plug‑in:
   ```ps1
   pwsh scripts\build_plugin.ps1
   ```
7. Drag `dist\RhinoSdRenderer.rhp` into Rhino 8.
