package com.ardritkrasniqi.prenotimi.ui.mainPage

import androidx.lifecycle.ViewModel
import org.threeten.bp.YearMonth

class MainViewModel : ViewModel() {
    
    
    val currentYear: YearMonth = YearMonth.now()
    val firstMonth = currentYear.withMonth(1)
    val lastMonth = currentYear.withMonth(12)
    
}
