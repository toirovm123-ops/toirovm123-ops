package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivityLog
import com.example.data.model.ChallengeWeek
import com.example.data.model.GroupInfo
import com.example.data.model.SavingsTransaction
import com.example.data.model.TravelPlan
import com.example.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}

@Dao
interface GroupDao {
    @Query("SELECT * FROM group_info WHERE groupCode = :code LIMIT 1")
    fun getGroupInfo(code: String = "BOT-GEO-7K29"): Flow<GroupInfo?>

    @Query("SELECT * FROM group_info LIMIT 1")
    suspend fun getGroupInfoSync(): GroupInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGroup(group: GroupInfo)

    @Query("UPDATE group_info SET isCompleted = :completed WHERE groupCode = :code")
    suspend fun setCompletedMode(code: String, completed: Boolean)

    @Query("UPDATE group_info SET streakCalculation = :mode WHERE groupCode = :code")
    suspend fun setStreakMode(code: String, mode: String)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<SavingsTransaction>>

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getTransactionsByUser(userId: String): Flow<List<SavingsTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: SavingsTransaction): Long

    @Update
    suspend fun updateTransaction(transaction: SavingsTransaction)

    @Delete
    suspend fun deleteTransaction(transaction: SavingsTransaction)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT SUM(amount) FROM transactions")
    fun getTotalSaved(): Flow<Double?>
}

@Dao
interface ActivityLogDao {
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<ActivityLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ActivityLog)

    @Query("DELETE FROM activity_logs")
    suspend fun clearLogs()
}

@Dao
interface TravelPlanDao {
    @Query("SELECT * FROM travel_plan WHERE id = 1 LIMIT 1")
    fun getTravelPlan(): Flow<TravelPlan?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(plan: TravelPlan)
}

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenge_weeks ORDER BY weekNumber ASC")
    fun getAllWeeks(): Flow<List<ChallengeWeek>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeks(weeks: List<ChallengeWeek>)

    @Update
    suspend fun updateWeek(week: ChallengeWeek)
}
