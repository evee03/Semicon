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
                val camera = modelViewer.camera
                val position = camera.getPosition(null)
                val forward = camera.getForwardVector(null)


                Log.d("ModelTransform", "Camera Pos: X=${position[0]}, Y=${position[1]}, Z=${position[2]}")
                Log.d("ModelTransform", "Camera Fwd: X=${forward[0]}, Y=${forward[1]}, Z=${forward[2]}")


        }
            true}

        val character = "models/deLorean.glb"
        Log.d("ModelRenderer", "Loading model: $character")

        createRenewables(character)




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