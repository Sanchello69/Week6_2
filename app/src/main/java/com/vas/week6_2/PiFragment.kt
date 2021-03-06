package com.vas.week6_2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vas.week6_2.databinding.FragmentPiBinding
import kotlinx.coroutines.*
import java.lang.Thread.sleep

class PiFragment : Fragment() {

    private var binding: FragmentPiBinding? = null

    private var counter = 0
    private var running = true

    private var job = Job()
    private val ioScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPiBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ioScope.launch {
            while (running){
                sleep(100)
                binding?.piTextView?.text = piSpigot(counter)
                counter ++
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun onClickStart(){
        Log.d("fragment_click", "start")
        running = true
        ioScope.launch {
            while (running){
                sleep(100)
                binding?.piTextView?.text = piSpigot(counter)
                counter ++
            }
        }
    }

    fun onClickPause(){
        Log.d("fragment_click", "pause")
        running = false
    }

    fun onClickStop(){
        Log.d("fragment_click", "stop")
        running = false
        counter = 0
        binding?.piTextView?.text = ""
    }

    /**
     * Код функции piSpigot взят из статьи
     * "«Краник», или алгоритм для поиска цифр числа Пи"
     * (https://habr.com/ru/post/188700/).
     */

    private suspend fun piSpigot(n: Int): String {
        return withContext(Dispatchers.IO){
            // найденные цифры сразу же будем записывать в StringBuilder
            val pi = StringBuilder(n)
            val boxes = n * 10 / 3 // размер массива
            val reminders = IntArray(boxes)
            // инициализируем масив двойками
            for (i in 0 until boxes) {
                reminders[i] = 2
            }
            var heldDigits = 0 // счётчик временно недействительных цифр
            for (i in 0 until n) {
                var carriedOver = 0 // перенос на следующий шаг
                var sum = 0
                for (j in boxes - 1 downTo 0) {
                    reminders[j] *= 10
                    sum = reminders[j] + carriedOver
                    val quotient = sum / (j * 2 + 1) // результат деления суммы на знаменатель
                    reminders[j] = sum % (j * 2 + 1) // остаток от деления суммы на знаменатель
                    carriedOver = quotient * j // j - числитель
                }
                reminders[0] = sum % 10
                var q = sum / 10 // новая цифра числа Пи
                // регулировка недействительных цифр
                if (q == 9) {
                    heldDigits++
                } else if (q == 10) {
                    q = 0
                    for (k in 1..heldDigits) {
                        var replaced = pi.substring(i - k, i - k + 1).toInt()
                        if (replaced == 9) {
                            replaced = 0
                        } else {
                            replaced++
                        }
                        pi.deleteCharAt(i - k)
                        pi.insert(i - k, replaced)
                    }
                    heldDigits = 1
                } else {
                    heldDigits = 1
                }
                pi.append(q) // сохраняем найденную цифру
            }
            if (pi.length >= 2) {
                pi.insert(1, '.') // добавляем в строчку точку после 3
            }
            pi.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("counter", counter)
        outState.putBoolean("running", running)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        counter = savedInstanceState?.getInt("counter") ?: 0
        running = savedInstanceState?.getBoolean("running") ?: true
        super.onViewStateRestored(savedInstanceState)
    }

}