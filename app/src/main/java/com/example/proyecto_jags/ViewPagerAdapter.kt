package com.example.proyecto_jags

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageAdapter (fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity){
    //Numero de pestañas
    override fun getItemCount(): Int = 3

    //Ddependiendo de la posición regresa un valor
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> Fragmento1()
            1-> fragmento2()
            2-> fragmento3()
            else -> Fragmento1()
        }
    }
}