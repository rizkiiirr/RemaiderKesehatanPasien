package com.example.remainderkesehatanpasien.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.remainderkesehatanpasien.data.alarm.AlarmManagerReminderScheduler
import com.example.remainderkesehatanpasien.data.alarm.ReminderScheduler
import com.example.remainderkesehatanpasien.data.local.AppDatabase
import com.example.remainderkesehatanpasien.data.local.dao.NewsDao
import com.example.remainderkesehatanpasien.data.local.dao.ReminderDao
import com.example.remainderkesehatanpasien.data.local.dao.UserDao
import com.example.remainderkesehatanpasien.data.remote.api.NewsApiService
import com.example.remainderkesehatanpasien.data.remote.source.NewsRemoteDataSource
import com.example.remainderkesehatanpasien.data.repository.CheckListRepositoryImpl
import com.example.remainderkesehatanpasien.data.repository.NewsRepositoryImpl
import com.example.remainderkesehatanpasien.data.repository.NoteRepositoryImpl
import com.example.remainderkesehatanpasien.data.repository.ReminderRepositoryImpl
import com.example.remainderkesehatanpasien.data.repository.UserRepositoryImpl
import com.example.remainderkesehatanpasien.domain.repository.CheckListRepository
import com.example.remainderkesehatanpasien.domain.repository.NewsRepository
import com.example.remainderkesehatanpasien.domain.repository.NoteRepository
import com.example.remainderkesehatanpasien.domain.repository.ReminderRepository
import com.example.remainderkesehatanpasien.domain.repository.UserRepository
import com.example.remainderkesehatanpasien.domain.usecase.*
import com.example.remainderkesehatanpasien.util.ImageStorageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- Providers untuk Database Lokal (Note) ---
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "note_db")
            .fallbackToDestructiveMigration() // <-- TAMBAHKAN INI UNTUK MENGHINDARI CRASH SAAT UPGRADE VERSI DB
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: AppDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao())
    }

    // Kita akan menyediakan semua use case dalam satu paket agar rapi
    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotesUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            addNote = AddNoteUseCase(repository),
            getNote = GetNoteUseCase(repository)
        )
    }

    // --- Providers untuk NewsAPI (Fetching Data Remote) ---    // URL dasar untuk NewsAPI
    private const val BASE_URL = "https://newsapi.org/v2/"

    // Menyediakan OkHttpClient dengan Logging Interceptor
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            // Level BODY akan mencetak request dan response body di Logcat.
            // Ganti ke NONE atau BASIC di produksi.
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS) // Contoh timeout koneksi
            .readTimeout(30, TimeUnit.SECONDS) // Contoh timeout baca
            .build()
    }

    // Menyediakan instance Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Menggunakan Gson untuk konversi JSON
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(db: AppDatabase): NewsDao {
        return db.newsDao()
    }

    // Menyediakan NewsApiService
    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    // Menyediakan NewsRemoteDataSource
    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(newsApiService: NewsApiService): NewsRemoteDataSource {
        return NewsRemoteDataSource(newsApiService)
    }

    // Menyediakan NewsRepository (implementasi)
    @Provides
    @Singleton
    fun provideNewsRepository(apiService: NewsApiService, newsDao: NewsDao): NewsRepository {
        return NewsRepositoryImpl(apiService, newsDao)
    }

    // Menyediakan GetHealthNewsUseCase
    @Provides
    @Singleton
    fun provideGetHealthNewsUseCase(repository: NewsRepository): GetHealthNewsUseCase {
        return GetHealthNewsUseCase(repository)
    }

    // --- Providers untuk User (Login/Register) ---

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(userRepository: UserRepository): RegisterUserUseCase {
        return RegisterUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideLoginUserUseCase(userRepository: UserRepository): LoginUserUseCase {
        return LoginUserUseCase(userRepository)
    }

    // --- Provider untuk Image Storage Manager ---
    @Provides
    @Singleton
    fun provideImageStorageManager(@ApplicationContext context: Context): ImageStorageManager {
        return ImageStorageManager(context)
    }

    // Tidak perlu menyediakan SessionManager secara eksplisit jika SessionManager sendiri dianotasi @Singleton dan @Inject constructor
    // Hilt akan otomatis membuat dan mengelola instance-nya.

    // --- Providers untuk Fitur Checklist ---
    @Provides
    @Singleton
    fun provideCheckListRepository(db: AppDatabase): CheckListRepository{
        return CheckListRepositoryImpl(db.checkListDao())
    }

    @Provides
    @Singleton
    fun provideCheckListUseCases(repository: CheckListRepository): CheckListUseCases{
        return CheckListUseCases(
            getItems = GetCheckListItemsUseCase(repository),
            addItem = AddCheckListItemsUseCase(repository),
            updateItem = UpdateCheckListItemsUseCase(repository),
            deleteItem = DeleteCheckListItemsUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideReminderDao(db: AppDatabase): ReminderDao {
        return db.reminderDao()
    }

    @Provides
    @Singleton
    fun provideReminderRepository(dao: ReminderDao): ReminderRepository {
        return ReminderRepositoryImpl(dao)
    }

    // Anda bisa juga membuat ReminderUseCases wrapper jika mau,
    // tapi untuk sekarang kita sediakan satu per satu.
    @Provides
    @Singleton
    fun provideAddReminderUseCase(repository: ReminderRepository): AddReminderUseCase {
        return AddReminderUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetRemindersUseCase(repository: ReminderRepository): GetRemindersUseCase {
        return GetRemindersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteReminderUseCase(repository: ReminderRepository): DeleteReminderUseCase {
        return DeleteReminderUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideReminderScheduler(@ApplicationContext context: Context): ReminderScheduler {
        return AlarmManagerReminderScheduler(context)
    }

    @Provides
    @Singleton
    fun provideSearchUseCase(
        noteRepository: NoteRepository,
        checklistRepository: CheckListRepository,
        reminderRepository: ReminderRepository
    ): SearchUseCase {
        return SearchUseCase(noteRepository, checklistRepository, reminderRepository)
    }
}