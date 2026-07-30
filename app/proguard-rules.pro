# --- OkHttp & Okio Rules ---
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# --- Gson Rules ---
# Mencegah ProGuard menghapus generic signatures dan anotasi SerializedName
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }

# Menjaga class model/POJO Anda agar variabel JSON tidak teracak (opsional tapi aman)
# Gantilah package di bawah jika Anda menggunakan class data/model khusus:
# -keep class com.mood.reaper.models.** { *; }
