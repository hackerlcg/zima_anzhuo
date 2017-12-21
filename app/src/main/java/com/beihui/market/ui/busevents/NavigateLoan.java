package com.beihui.market.ui.busevents;


/**
 * EventBus event for navigating to TabLoan with param query money
 */
public class NavigateLoan {

    public int queryMoney = -1;

    public NavigateLoan(int queryMoney) {
        this.queryMoney = queryMoney;
    }

}
