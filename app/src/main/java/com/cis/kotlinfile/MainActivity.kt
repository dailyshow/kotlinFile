package com.cis.kotlinfile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Exception

// 안드로이드는 애플리케이션이 데이터를 저장할 수 있는 저장소를 두 가지로 제공하고있다.
// 내부 저장소 : 애플리케이션을 통해서만 접근이 가능
// 내부 저장소는 권한이 필요하지 않다.
// openFileOutput, openFileInput
// 외부 저장소 : 단말기 내부의 공유 영역으로 모든 애플리케이션이 접근 가능하다.
// 외부 저장소는 권한이 필요하다.
// FileInputStream, FileOutputStream
class MainActivity : AppCompatActivity() {
    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    var path : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        path = Environment.getExternalStorageDirectory().absolutePath + "/android/data/" + packageName
        val file = File(path)
        if (file.exists() == false) {
            file.mkdir()
        }

        checkPermission()

        storageWriteBtn.setOnClickListener { view ->
            try {
                val output = openFileOutput("myFile.dat", Context.MODE_PRIVATE)
                val dos = DataOutputStream(output)
                dos.writeInt(100)
                dos.writeDouble(33.33)
                dos.writeUTF("내부 저장소")
                dos.flush()
                dos.close()

                tv.text = "저장 완료"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        storageReadBtn.setOnClickListener { view ->
            try {
                val input = openFileInput("myFile.dat")
                val dis = DataInputStream(input)
                val value1 = dis.readInt()
                val value2 = dis.readDouble()
                val value3 = dis.readUTF()
                dis.close()

                tv.text = "value1 : ${value1}\n"
                tv.append("value2 : ${value2}\n")
                tv.append("value3 : ${value3}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        extStorageWriteBtn.setOnClickListener { view ->
            try {
                val output = FileOutputStream(path + "/sdFile.dat")
                val dos = DataOutputStream(output)
                dos.writeInt(200)
                dos.writeDouble(55.55)
                dos.writeUTF("외부 저장소")
                dos.flush()
                dos.close()

                tv.text = "저장 완료"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        extStorageReadBtn.setOnClickListener { view ->
            try {
                val input = FileInputStream(path + "/sdFile.dat")
                val dis = DataInputStream(input)
                val value1 = dis.readInt()
                val value2 = dis.readDouble()
                val value3 = dis.readUTF()
                dis.close()

                tv.text = "value1 : ${value1}\n"
                tv.append("value2 : ${value2}\n")
                tv.append("value3 : ${value3}")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }

        for (permission : String in permissionList ) {
            val chk = checkCallingOrSelfPermission(permission)
            if (chk == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permissionList, 0)
                break
            }
        }
    }
}
