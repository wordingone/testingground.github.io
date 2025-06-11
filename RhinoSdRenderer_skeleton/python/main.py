"""Python bridge between Rhino (.NET) and Stable‑Diffusion 3.5.

The C# side passes:
    • PNG bytes of the viewport (flat shaded)
    • text prompt (str)

We return:
    • System.Drawing.Bitmap (via pythonnet interop)
"""
import io, os, torch, numpy as np
from PIL import Image
from diffusers import StableDiffusionControlNetPipeline, ControlNetModel
from controlnet_aux import CannyDetector

# --- lazy global initialisation ---
pipe = None
def _init():
    global pipe, canny
    if pipe is not None:
        return
    model_path   = os.getenv("SDXL_PATH",   "../models/sd35_medium")
    control_path = os.getenv("CANNY_PATH",  "../models/canny_cn")
    controlnet   = ControlNetModel.from_pretrained(control_path, torch_dtype=torch.float16)
    pipe         = StableDiffusionControlNetPipeline.from_pretrained(
                       model_path, controlnet=controlnet,
                       torch_dtype=torch.float16).to("cuda")
    pipe.enable_xformers_memory_efficient_attention()
    canny = CannyDetector()

_init()

def render_image(png_bytes: bytes, prompt: str):
    """Entry point called from C#; returns a Bitmap."""
    _init()
    with Image.open(io.BytesIO(png_bytes)) as img:
        img = img.convert("RGB")
    # Generate Canny edge map
    edge = canny(img, low_threshold=100, high_threshold=200)
    edge = Image.fromarray(edge).convert("RGB").resize((768, 768))

    res  = pipe(prompt=prompt,
                num_inference_steps=1,
                image=edge,
                guidance_scale=0.8)
    out  = res.images[0].resize(img.size, Image.LANCZOS)

    # Convert PIL → Bitmap via memory stream (pythonnet)
    import System.Drawing as SD
    ms = SD.Imaging.MemoryStream()
    out.save(ms, format="PNG")
    return SD.Bitmap(ms)


if __name__ == "__main__":
    import argparse
    parser = argparse.ArgumentParser(description="Render an image using the SD pipeline")
    parser.add_argument("input", help="Input PNG file")
    parser.add_argument("--prompt", required=True, help="Text prompt")
    parser.add_argument("-o", "--output", default="out.png", help="Output file")
    args = parser.parse_args()

    with open(args.input, "rb") as f:
        bmp = render_image(f.read(), args.prompt)
    bmp.Save(args.output)
