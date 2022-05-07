
# Artose
![Logo](https://user-images.githubusercontent.com/39225800/167268896-e2f03a59-71f0-4ea3-96db-424806d2a295.png)  
**A** **R***eal*-**T***ime* **O***cean* **S***imulation* **E***ngine* written in Java that uses **OpenGL** and **OpenCL**.
## Description
This is an implementation of the [Gerstner waves](https://en.wikipedia.org/wiki/Trochoidal_wave) algorithm in Java that uses a renderer I wrote in **OpenGL** and **OpenCL**.

### 📚 Note
This project is meant for educational purposes. During the development I referenced the [Crest Unity Addon](https://github.com/wave-harmonic/crest) in order to understand the Gerstner waves algorithm as well as the general graphics pipeline and it's optimization.

## :framed_picture: Gallery
### Day Scene
![Day Scene](https://user-images.githubusercontent.com/39225800/167267773-23bed5ee-17d4-4f5c-b20f-8fdaf618c5d7.gif)
### Golden Hour Scene
![Golden Hour Scene](https://user-images.githubusercontent.com/39225800/167268061-49f52d07-b171-47ba-a040-a87e61a1d30f.gif)
### Night Scene
![Night Scene](https://user-images.githubusercontent.com/39225800/167268067-86d0b780-d425-4a28-8390-d9a93bd3df03.gif)

# :hammer_and_wrench: Technical Details

- Uses an OpenGL renderer
- Uses OpenCL kernels to combine different wave frequencies into one texture
- Implements a Level of Detail system
- Has Sub Surface Scattering
- The Ocean mesh follows the camera allowing for infinite Oceans
- Currently only works on MacOS
