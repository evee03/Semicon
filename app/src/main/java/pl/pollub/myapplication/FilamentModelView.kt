package pl.pollub.myapplication

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.filament.Texture
import com.google.android.filament.TextureSampler
import com.google.android.filament.View
import com.google.android.filament.android.UiHelper
import com.google.android.filament.utils.KTX1Loader
import com.google.android.filament.utils.ModelViewer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ModelRenderer {
    private lateinit var surfaceView: SurfaceView
    private lateinit var lifecycle: Lifecycle

    private lateinit var choreographer: Choreographer
    private lateinit var uiHelper: UiHelper

    private lateinit var modelViewer: ModelViewer


    private val assets: AssetManager
        get() = surfaceView.context.assets

    private val frameScheduler = FrameCallback()

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            choreographer.postFrameCallback(frameScheduler)
        }

        override fun onPause(owner: LifecycleOwner) {
            choreographer.removeFrameCallback(frameScheduler)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            choreographer.removeFrameCallback(frameScheduler)
            lifecycle.removeObserver(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onSurfaceAvailable(surfaceView: SurfaceView, lifecycle: Lifecycle) {
        this.surfaceView = surfaceView
        this.lifecycle = lifecycle

        lifecycle.addObserver(lifecycleObserver)

        choreographer = Choreographer.getInstance()
        uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
            // Przezroczyste tło
            isOpaque = true
        }

        modelViewer = ModelViewer(surfaceView = surfaceView, uiHelper = uiHelper)

        // Ustawienie przezroczystego tła
        modelViewer.scene.skybox = null
        modelViewer.view.blendMode = View.BlendMode.TRANSLUCENT
        modelViewer.renderer.clearOptions = modelViewer.renderer.clearOptions.apply {
            clear = true
            //clearColor = floatArrayOf(8.0f, 12.0f, 9.0f, 1.0f) // Całkowicie przezroczyste tło RGBA
            //clearColor = floatArrayOf(255f,8f,12f,0.04f)
            //clearColor = floatArrayOf(0.04f,12f,8f,255f)
        }

        modelViewer.view.apply {
            renderQuality = renderQuality.apply {
                hdrColorBuffer = View.QualityLevel.ULTRA
            }
        }


        surfaceView.setOnTouchListener { _, event ->
            modelViewer.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_MOVE) {
                val camera = modelViewer.camera // Get the camera managed by ModelViewer
                val position = camera.getPosition(null) // Returns FloatArray(3) [x, y, z]
                val forward = camera.getForwardVector(null) // Returns FloatArray(3) [x, y, z] pointing where the camera looks
                // You can also get camera.upVector or camera.leftVector if needed

                // Log the values using a specific tag
                Log.d("ModelTransform", "Camera Pos: X=${position[0]}, Y=${position[1]}, Z=${position[2]}")
                Log.d("ModelTransform", "Camera Fwd: X=${forward[0]}, Y=${forward[1]}, Z=${forward[2]}")


        }
            true}

        val character = "models/deLorean.glb"
        Log.d("ModelRenderer", "Loading model: $character")

        createRenewables(character)

        // Przybliżenie modelu przez zmianę pozycji kamery
        //modelViewer.camera.lookAt(-1.0, -1.0, 1.0, -1.0, -1.0, -1.0, -1.0, 1.0, -1.0);        // Usunięcie tworzenia światła z skyboxem
        // createLight()


        val engine = modelViewer.engine
        val scene = modelViewer.scene
        val lightSource = "envs/venetian_crossroads_2k/venetian_crossroads_2k_ibl.ktx"

        readCompressedAsset(lightSource).let {
            scene.indirectLight = KTX1Loader.createIndirectLight(engine, it)
            scene.indirectLight!!.intensity = 30000.0f
        }
    }

    fun createRenewables(name: String) {

        val buffer = assets.open(name).use { input ->
            val bytes = ByteArray(input.available())
            input.read(bytes)
            ByteBuffer.allocateDirect(bytes.size).apply {
                order(ByteOrder.nativeOrder())
                put(bytes)
                rewind()
            }
        }

        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
        modelViewer.camera.setScaling(2.0,2.0)

    }

    private fun createLight(){
        val engine = modelViewer.engine
        val scene = modelViewer.scene
        val lightSource = "envs/venetian_crossroads_2k/venetian_crossroads_2k_ibl.ktx"

        readCompressedAsset(lightSource).let {
            scene.indirectLight = KTX1Loader.createIndirectLight(engine,it)
            scene.indirectLight!!.intensity = 30_000.0f

        }
        val lightSource2 = "envs/venetian_crossroads_2k/venetian_crossroads_2k_skybox.ktx"
        readCompressedAsset(lightSource2).let {
            scene.skybox = KTX1Loader.createSkybox(engine,it)


        }
    }
//fun createRenewables(name: String) {
//    val buffer = assets.open(name).use { input ->
//        val bytes = ByteArray(input.available())
//        input.read(bytes)
//        ByteBuffer.allocateDirect(bytes.size).apply {
//            order(ByteOrder.nativeOrder())
//            put(bytes)
//            rewind()
//        }
//    }
//
//    // Consider loading the texture *before* the model or ensuring it's ready
//    val texture = loadPngTexture("textures/textures.png") // Load your texture
//
//    modelViewer.loadModelGlb(buffer)
//    modelViewer.transformToUnitCube()
//    modelViewer.camera.setScaling(2.0, 2.0) // Adjust scaling as needed
//
//    // It's often better to wait until the model is fully loaded if modifying materials.
//    // model-viewer might handle this, but for direct engine manipulation, be cautious.
//    // You might need a listener or check asset properties if issues persist.
//
//    val engine = modelViewer.engine
//    val scene = modelViewer.scene // Get the scene
//
//    Log.d("renderables", "Processing model: $name")
//
//    // Ensure asset and its entities are ready.
//    // modelViewer.asset might be non-null before entities are fully processed by engine.
//    // If problems persist, investigate model loading completion callbacks/state.
//    val asset = modelViewer.asset ?: return // Asset might be null if loading failed
//    val renderables = asset.renderableEntities // Get Entities with renderable components
//
//    if (renderables.isEmpty()) {
//        Log.w("renderables", "Model '$name' has no renderable entities.")
//        return
//    }
//
//    val rm = engine.renderableManager
//
//    // --- Corrected Loop ---
//    for (entity in renderables) {
//        val instance = rm.getInstance(entity) // Get the Renderable Instance handle
//        if (instance == 0) {
//            // This entity doesn't have a valid Renderable component instance known to the manager
//            Log.w("renderables", "Skipping entity $entity: No valid RenderableManager instance.")
//            continue
//        }
//
//        // Check how many primitives this renderable has (optional but good practice)
//        val primitiveCount = rm.getPrimitiveCount(instance)
//        if (primitiveCount == 0) {
//            Log.w("renderables", "Skipping entity $entity (instance $instance): Has 0 primitives.")
//            continue
//        }
//
//        // Assuming you want to modify the material of the first primitive (index 0)
//        if (primitiveCount > 0) {
//            Log.d("renderables", "Processing entity $entity (instance $instance), primitive 0")
//            // *** Use the 'instance' handle here, not 'entity' ***
//            val materialInstance = rm.getMaterialInstanceAt(instance, 0)
//
//            if (materialInstance == null) {
//                Log.e("renderables", "Entity $entity (instance $instance), Primitive 0 has NULL material instance!")
//                continue // Skip if no material instance found
//            }
//
//            // Now it's safe to set the parameter
//            if (texture != null) {
//                Log.d("renderables", "Applying texture to material instance of entity $entity")
//                // Make sure the parameter name "baseColorMap" is correct for your model's material
//                try {
//                    materialInstance.setParameter(
//                        "baseColorMap", // Or "albedoMap", check your material definition
//                        texture,
//                        TextureSampler() // Use default sampler settings
//                    )
//                } catch (e: Exception) {
//                    Log.e("renderables", "Failed to set texture parameter for entity $entity: ${e.message}", e)
//                    // This might happen if the parameter name is wrong or types mismatch
//                }
//            } else {
//                Log.w("renderables", "Texture is null, cannot apply to entity $entity.")
//            }
//        } else {
//            // Handle cases where you might want to iterate over all primitives if > 1
//            Log.d("renderables", "Entity $entity (instance $instance) has multiple primitives ($primitiveCount). Currently only processing primitive 0.")
//            // You could loop from 0 until primitiveCount here if needed
//        }
//    }
//    Log.d("renderables", "Finished processing renderables for $name")
//}

    // Your loadPngTexture function seems okay, but ensure the texture format
// (SRGB8_A8) is appropriate. RGBA8 might also work depending on your needs.
// Keep the existing loadPngTexture function as it is for now.
    fun loadPngTexture(name: String): Texture? {
        // ... (your existing implementation)
        try {
            assets.open(name).use { input ->
                val rawBitmap = BitmapFactory.decodeStream(input)
                val bitmap = rawBitmap?.copy(Bitmap.Config.ARGB_8888, false)

                if (bitmap == null) {
                    Log.e("Texture", "Failed to decode $name")
                    return null
                }
                val buffer = ByteBuffer.allocateDirect(bitmap.byteCount)
                buffer.order(ByteOrder.nativeOrder()) // Use native order
                bitmap.copyPixelsToBuffer(buffer)
                buffer.rewind()

                val texture = Texture.Builder()
                    .width(bitmap.width)
                    .height(bitmap.height)
                    .levels(1) // No mipmaps for basic loading
                    .sampler(Texture.Sampler.SAMPLER_2D)
                    // SRGB8_A8 is often correct for color textures.
                    // If colors look wrong, try Texture.InternalFormat.RGBA8
                    .format(Texture.InternalFormat.SRGB8_A8)
                    .build(modelViewer.engine)

                texture.setImage(
                    modelViewer.engine,
                    0, // Level 0
                    Texture.PixelBufferDescriptor(
                        buffer,
                        Texture.Format.RGBA, // Format of the data in the buffer
                        Texture.Type.UBYTE   // Type of the data in the buffer
                        // Optional: Add a handler to free the buffer when Filament is done
                        // , AndroidTextureHelper.textureCleanup(buffer)
                    )
                )

                // Optional: Generate mipmaps if needed for better quality at distances
                // texture.generateMipmaps(modelViewer.engine)

                Log.d("Texture", "Loaded texture: $name -> $texture")
                bitmap.recycle() // Recycle the Android bitmap once data is in buffer
                return texture
            }
        } catch (e: IOException) {
            Log.e("Texture", "Error loading texture $name: ${e.message}", e)
            return null
        }
    }


    private fun readCompressedAsset(name:String) : ByteBuffer{

        val input = assets.open(name)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)

    }
    inner class FrameCallback : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)
            modelViewer.render(frameTimeNanos)
        }
    }
}