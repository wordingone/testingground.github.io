"""Download all required model weights via Hugging Face.

Usage:
    python scripts/download_models.py
"""
from huggingface_hub import snapshot_download

MODELS = {
    "sd35_large":   "stabilityai/stable-diffusion-3.5-large",
    "sd35_medium":  "stabilityai/stable-diffusion-3.5-medium",
    "canny_cn":     "lllyasviel/control_v11p_sd15_canny",
}

for local_name, repo in MODELS.items():
    print(f"📥  Downloading {repo} …")
    snapshot_download(repo,
                      local_dir=f"models/{local_name}",
                      local_dir_use_symlinks=False,
                      resume_download=True)
print("✅  All models downloaded.  You may now build the plug‑in.")
