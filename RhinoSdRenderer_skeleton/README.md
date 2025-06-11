# RhinoSdRenderer — Live Stable‑Diffusion 3.5 Renderer for Rhino 8
*(auto‑generated skeleton on 2025-06-11)*

## Quick‑start (Windows 10/11 ×64)

```ps1
git clone https://github.com/YourUser/RhinoSdRenderer.git
cd RhinoSdRenderer
python -m venv venv; venv\Scripts\activate
pip install -r python\requirements.txt
python scripts\download_models.py    # ≈ 10 GB download via Git LFS
pwsh scripts\build_plugin.ps1        # produces RhinoSdRenderer.rhp in dist/
```

Then drag‑and‑drop `dist\RhinoSdRenderer.rhp` into an open Rhino 8 window.

> **Note** : The first render will be slow while PyTorch compiles kernels. Subsequent renders are realtime.

## Project layout
```text
plugin/      – C# /.NET 7 Rhino plug‑in source
python/      – Python inference bridge (PyTorch + diffusers)
models/      – Downloaded SD3.5 + ControlNet weights (git‑ignored)
scripts/     – Utility scripts (model DL, build, CI helpers)
docs/        – High‑level architecture & setup docs
.github/     – CI workflow
```

---
