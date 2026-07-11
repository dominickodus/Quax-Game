# Builds and runs the Quax JavaFX game.
# Usage: .\run.ps1   (or double-click run.bat)

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
$fx = Join-Path $root "javafx-sdk-21.0.10\lib"
$out = Join-Path $root "out\production\Quax"
$modules = "javafx.controls,javafx.fxml,javafx.media,javafx.swing,javafx.web"

New-Item -ItemType Directory -Force -Path $out | Out-Null

Write-Host "Compiling..."
$sources = Get-ChildItem (Join-Path $root "src") -Filter "*.java" -File
& javac --module-path $fx --add-modules $modules -d $out $sources.FullName
if ($LASTEXITCODE -ne 0) { throw "Compilation failed" }

Copy-Item (Join-Path $root "src\assets") $out -Recurse -Force
Copy-Item (Join-Path $root "src\META-INF") $out -Recurse -Force

Write-Host "Launching..."
& java --module-path $fx --add-modules $modules -cp $out Main
