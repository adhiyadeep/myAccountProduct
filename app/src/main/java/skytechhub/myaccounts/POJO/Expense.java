package skytechhub.myaccounts.POJO;

import java.util.Date;

/**
 * Created by Deep on 28-09-2017.
 */
public class Expense {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getChequeno() {
        return chequeno;
    }

    public void setChequeno(String chequeno) {
        this.chequeno = chequeno;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getGivento() {
        return givento;
    }

    public void setGivento(String givento) {
        this.givento = givento;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getDMLDateTime() {
        return DMLDateTime;
    }

    public void setDMLDateTime(String DMLDateTime) {
        this.DMLDateTime = DMLDateTime;
    }

    private String id;
    private Date date;
    private String billno;
    private String billdate;
    private String amount;
    private String paymentmode;
    private String chequeno;
    private String bankname;
    private String givento;
    private String remark;
    private String companyname;
    private String DMLDateTime;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    private String purpose;
}

