package com.example.cotizadorapp.pdf

import android.content.Context
import android.graphics.*
import android.os.Environment
import com.example.cotizadorapp.model.DetalleServicioEntity
import com.example.cotizadorapp.model.ServicioEntity
import com.example.cotizadorapp.model.PerfilEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PdfGenerator {

    fun generarPdf(
        context: Context,
        perfil: PerfilEntity,
        servicio: ServicioEntity,
        detalles: List<DetalleServicioEntity>
    ): File? {
        try {
            val fechaActual = Date()
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaEmision = formatoFecha.format(fechaActual)
            val calendario = Calendar.getInstance()
            calendario.time = fechaActual
            calendario.add(Calendar.MONTH, 1)
            val fechaVencimiento = formatoFecha.format(calendario.time)

            val nombreArchivo = "${servicio.cliente}_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(fechaActual)}.pdf"
            val directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!directorio.exists()) directorio.mkdirs()
            val archivoPdf = File(directorio, nombreArchivo)
            val fos = FileOutputStream(archivoPdf)

            val pdf = android.graphics.pdf.PdfDocument()
            val paginaInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val pagina = pdf.startPage(paginaInfo)
            val canvas = pagina.canvas

            val paint = Paint()
            val tituloPaint = Paint().apply {
                textSize = 24f
                typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
                color = Color.BLACK
                textAlign = Paint.Align.CENTER
            }

            var y = 60f

            // TÍTULO
            canvas.drawText("PRESUPUESTO", paginaInfo.pageWidth / 2f, y, tituloPaint)
            y += 30f

            paint.apply {
                textSize = 12f
                color = Color.DKGRAY
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                textAlign = Paint.Align.LEFT
            }

            // FECHAS
            canvas.drawText("Fecha emisión: $fechaEmision", 40f, y, paint)
            canvas.drawText("Vencimiento: $fechaVencimiento", 400f, y, paint)
            y += 25f

            // PERFIL
            paint.color = Color.BLACK
            paint.textSize = 14f
            canvas.drawText("Perfil:", 40f, y, paint); y += 18f
            paint.textSize = 12f
            canvas.drawText("Nombre: ${perfil.nombre}", 60f, y, paint); y += 15f
            canvas.drawText("Tel: ${perfil.tel}", 60f, y, paint); y += 15f
            canvas.drawText("Email: ${perfil.email}", 60f, y, paint); y += 25f

            // CLIENTE Y SERVICIO
            paint.textSize = 14f
            canvas.drawText("Cliente: ${servicio.cliente}", 40f, y, paint); y += 20f
            canvas.drawText("Descripción del servicio:", 40f, y, paint); y += 15f
            paint.textSize = 12f
            canvas.drawText(servicio.descripcion, 60f, y, paint); y += 25f

            // SEPARADOR
            paint.color = Color.LTGRAY
            paint.strokeWidth = 1f
            canvas.drawLine(40f, y, 555f, y, paint)
            y += 15f

            // DETALLES
            paint.color = Color.BLACK
            paint.textSize = 12f
            canvas.drawText("DETALLES DEL SERVICIO", 40f, y, paint)
            y += 20f

            // Tabla: Descripción | Costo
            detalles.forEachIndexed { i, d ->
                if (y > 780f) {
                    pdf.finishPage(pagina)
                    val nuevaPaginaInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, pdf.pages.size + 1).create()
                    val nuevaPagina = pdf.startPage(nuevaPaginaInfo)
                    y = 60f
                }
                canvas.drawText(d.descripcion, 50f, y, paint)
                canvas.drawText("$${d.costo}", 500f, y, paint)
                y += 18f
            }

            // SEPARADOR FINAL
            paint.color = Color.LTGRAY
            canvas.drawLine(40f, y, 555f, y, paint)
            y += 25f

            // TOTAL
            paint.apply {
                color = Color.BLACK
                textSize = 16f
                typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            }
            canvas.drawText("COSTO TOTAL: $${servicio.costoTotal}", 40f, y, paint)

            pdf.finishPage(pagina)
            pdf.writeTo(fos)
            pdf.close()
            fos.close()

            return archivoPdf

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

