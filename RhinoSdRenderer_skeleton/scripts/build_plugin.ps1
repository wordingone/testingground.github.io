<#  Build the C# plug‑in, copy dependencies, and package an .rhi installer. #>

Param(
    [string]$Configuration = "Release"
)

Write-Host "🔧 Building C# project …"
dotnet build plugin/RhinoSdRenderer.csproj -c $Configuration

$dist = "dist"
if (!(Test-Path $dist)) { New-Item $dist -ItemType Directory | Out-Null }

# Copy the RHP and managed DLLs
Get-ChildItem plugin/bin/$Configuration/net7.0-windows/*.rhp | Copy-Item -Destination $dist -Force
Get-ChildItem plugin/bin/$Configuration/net7.0-windows/*.dll | Copy-Item -Destination $dist -Force

Write-Host "📦 Packing .rhi … (placeholder – implement Rhino RHI packaging here)"
# TODO: call Rhino Installer Engine CLI or zip as .rhi

Write-Host "✅ Done.  Output in dist/"
