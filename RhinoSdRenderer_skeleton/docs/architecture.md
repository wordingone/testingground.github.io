## High‑level architecture

```
Rhino (C#) ──► Viewport FBO ─╮
                             │ PNG bytes
                             ▼
                   Python bridge (pythonnet)
                             │ edge map (Canny)
                             ▼
             SD 3.5‑Medium + ControlNet (CUDA)
                             │ JPEG bytes
                             ▼
               Rhino RenderWindow (C# bitmap)
```

* Zero‑copy ideals: future version should pass CUDA pointers directly via DLPack.
* Preview loop is throttled to maintain Rhino UI responsiveness.
* Seeds are deterministically derived from camera matrix for temporal coherence.
