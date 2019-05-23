import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Paige
 */
@ManagedBean(name = "taxCalculation")
@RequestScoped
public class PSIberTaxCalculator implements Serializable {
    String pattern = "###,###.##";
    DecimalFormat df = new DecimalFormat(pattern);
    private static final String MONTHLY_OR_ANNUALLY = "Monthly|1; Annually|2" ;
    private static final String FOR_TAX_YEAR = "2017|2017;2018|2018";
    
    private int salaryMonthlyOrAnnually, noOfDependants,age, taxYear;    
    private double monthlyPAYE = 0.0d, annualPAYE = 0.0d, taxCredits = 0.0d, annualTaxCredits = 0.0d,
    PAYEAnnualy = 0.0d, PAYEMonthly = 0.0d, NettMonthly = 0.0d, NettAnnually = 0.0d;   

   
   
    private String calculatedResponse;  
    private boolean hasContribution;  

    
    private double income, totalTaxableEarnings, taxRebates, taxThreshold, medicalTaxCredits;            
        
  
     
    
    private Map<String, String> getOptionsMapString(String map) {
        Map<String, String> options = new LinkedHashMap<>();
        for (String splitOptions : map.split(";")) {
            String[] option = splitOptions.split("\\|");
            if (option.length > 1) {
                options.put(option[1], option[0]);
            } else {
                options.put("", option[0]);
            }
        }
        return options;
    }
    
    
    public Map<String, String> getTaxYearOptions() {
        return getOptionsMapString(FOR_TAX_YEAR);
    }
    public Map<String, String> getMonthlyOrAnnuallyOptions() {
        return getOptionsMapString(MONTHLY_OR_ANNUALLY);
    }
    
    
    private boolean validateUserInput() {
        if (age == 0) {
            returnError("Ensure an age is entered.");
            return false;
        }
        
        if (income == 0) {
            returnError("Ensure your income is entered.");
            return false;
        }
        
        if (noOfDependants < 0) {
            returnError("Please enter the number of dependents you have.");
            return false;
        }
        
        return true;
    }
     
    public boolean taxThreshold(int age ,int year, double income ){
        if (year == 2017){
            if ((age < 65 && income <=75000) 
                || ((age > 65 && age < 75) && income <= 116150) 
                || (age >= 75 && income <=129850)) {
                return true;
            } else {
                return false;
            }
        } else if (year == 2018){
            if ((age < 65 && income <=75750) 
                || ((age > 65 && age < 75) && income <= 117300) 
                || (age >= 75 && income <=131150)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
        public double getTaxRebate(int taxyear, int age){
        double taxRebate = 0.0d;
        if (taxyear == 2017){
            taxRebate = 13500;
            if (age > 65 && age < 75){                    
                taxRebate += 7407;
            } else if (age >= 75){                    
                taxRebate += 2466;
            }
        } else if (taxyear == 2018){
            taxRebate = 13635;
            if (age > 65 && age < 75){                    
                taxRebate += 7479;
            } else if (age >= 75){                    
                taxRebate += 2493;
            }
        }                 
        return taxRebate;        
    }
        
    public double calculateTax(int age, double income, int taxyear ) {         
        double taxVar = 0.0d;
        double incomeThresDif = 0.0d;
        double taxMonthly = 0.0d;
        double percTax = 0.0d;
        double rateOfTax = 0.0d;
        double threshold = 0.0d;
        double TAXrebate = getTaxRebate(taxyear, age);
        switch (taxyear) {
            case 2017:                 
                    if (income > 0 && income <= 188000.0) {
                        percTax = 0.18;
                        rateOfTax = 0;
                        threshold = 0.0;
                    } else if(income <= 293600.0) {
                        percTax = 0.26;
                        rateOfTax = 33840;
                        threshold = 188000.0;
                    } else if(income <= 406400.0) {
                        percTax = 0.31;
                        rateOfTax = 61296;
                        threshold = 293600.0;
                    } else if(income <= 550100.0) {
                        percTax = 0.36;
                        rateOfTax = 96264;
                        threshold = 406400.0;
                    } else if(income <= 701300.0) {
                        percTax = 0.39;
                        rateOfTax = 147996;
                        threshold = 550100.0;
                    } else if(income >= 701301.0) {
                        percTax = 0.41;
                        rateOfTax = 206964;
                        threshold = 701300.0;
                    }  
                    incomeThresDif = income - threshold;
                    taxVar = (((incomeThresDif * percTax) + rateOfTax) - TAXrebate);
                    taxMonthly = taxVar / 12;                                                
                    return taxMonthly;
                
            case 2018:               
                    if (income > 0 && income <= 189880.0) {
                        percTax = 0.18;
                        rateOfTax = 0;
                        threshold = 0.0;
                    } else if(income <= 296540.0) {
                        percTax = 0.26;
                        rateOfTax = 34178;
                        threshold = 189880.0;
                    } else if(income <= 410460.0) {
                        percTax = 0.31;
                        rateOfTax = 61910;
                        threshold = 296540.0;
                    } else if(income <= 555600.0) {
                        percTax = 0.36;
                        rateOfTax = 97225;
                        threshold = 410460.0;
                    } else if(income <= 708310.0) {
                        percTax = 0.39;
                        rateOfTax = 149475;
                        threshold = 555600.0;
                    } else if(income <= 1500000.0) {
                        percTax = 0.41;
                        rateOfTax = 209032;
                        threshold = 708310.0;
                    }  else if(income > 1500000.0) {
                        percTax = 0.45;
                        rateOfTax = 533625;
                        threshold = 1500000.0;
                    }
                    incomeThresDif = income - threshold;
                    taxVar = (((incomeThresDif * percTax) + rateOfTax) - TAXrebate);
                    taxMonthly = taxVar / 12;                                
            return taxMonthly;              
            default:            
            break;
        }            
      return taxMonthly;
    }
    
    
    private double getTaxCredit(boolean hasMedicalContribution, int noOfDependants, int year){
        double taxCredit = 0.0d;
        if (year == 2017){
            
            if (hasMedicalContribution) {
                taxCredit = 286.0;
                if(noOfDependants == 0){
                    return taxCredit;
                }
                if (noOfDependants == 1){
                   return taxCredit*2;
                } 
                if (noOfDependants > 1) {
                    taxCredit = (taxCredit*2);
                    for (int i = 0; i < (noOfDependants-1); i++) {                                                 
                        taxCredit = taxCredit  + 192;
                    }                    
                    return taxCredit;
                }
            }                            
        } else if (year == 2018){
            
            if (hasMedicalContribution) {
                taxCredit = 303.0;
                if(noOfDependants == 0){
                    return taxCredit;
                }
                if (noOfDependants == 1){
                   return taxCredit*2;
                } 
                if (noOfDependants > 1) {
                    taxCredit = (taxCredit*2);
                    for (int i = 0; i < (noOfDependants-1); i++) {                                                 
                        taxCredit = taxCredit  + 204;
                    }                    
                    return taxCredit;
                }
            }   
        }
        return taxCredit;       
    } 
    
    public void performCalculation() {
        if (validateUserInput()) {
            taxCredits = Math.abs(getTaxCredit(hasContribution, noOfDependants, taxYear));
            annualTaxCredits = Math.abs((getTaxCredit(hasContribution, noOfDependants, taxYear)*12));
             if (salaryMonthlyOrAnnually == 1){ 
                 totalTaxableEarnings = income *12;
              } else if (salaryMonthlyOrAnnually == 2){
                  totalTaxableEarnings = income;
              }

                 if (!(taxThreshold(age ,taxYear, totalTaxableEarnings )))  {
                    monthlyPAYE = calculateTax(age, totalTaxableEarnings, taxYear);
                    annualPAYE = monthlyPAYE *12;

                    PAYEMonthly = monthlyPAYE - taxCredits;
                    PAYEAnnualy = annualPAYE - (taxCredits *12); 

                    NettMonthly = (totalTaxableEarnings / 12) - PAYEMonthly;
                    NettAnnually = totalTaxableEarnings - (annualPAYE - (annualTaxCredits));
                 } else if (taxThreshold(age, taxYear, totalTaxableEarnings )) {
                    monthlyPAYE = 0.0;
                    annualPAYE = 0.0;

                    PAYEMonthly = 0.0;
                    PAYEAnnualy = 0.0;

                    NettMonthly = (totalTaxableEarnings / 12) ;
                    NettAnnually = totalTaxableEarnings ;
                 }                                 

             StringBuilder stringBuilder = new StringBuilder();                  
                 try {
                    stringBuilder                                       
                        .append("<table class=\"table table-condensed nowrap\" width=\"100%\" cellspacing=\"0\">")
                            .append("<thead>")
                                .append("<tr class=\"pt-3\">")
                                    .append("<th>")
                                        .append(" ")
                                    .append("</th>")
                                    .append("<th style=\"color:#0074f2;\" class=\"text-right\">")
                                        .append("Monthly (R)")
                                    .append("</th>")
                                    .append("<th style=\"color:#0074f2;\" class=\"text-right\">")
                                        .append("Annually (R)")
                                    .append("</th>")
                                .append("</tr>")
                            .append("<t/head>")
                            .append("<tbody>")
                                .append("<tr>")
                                    .append("<td style=\"color:#0074f2;\">Calculated PAYE before tax credits</td>")
                                    .append("<td class=\"text-right\">" + df.format(monthlyPAYE) +"</td>")
                                    .append("<td class=\"text-right\">" +df.format(annualPAYE) +"</td>")
                                .append("</tr>")
                                .append("<tr>")
                                    .append("<td style=\"color:#0074f2;\">Tax Credits</td>")                            
                                    .append("<td class=\"text-right\">" + df.format(taxCredits) +"</td>")                                
                                    .append("<td class=\"text-right\">" + df.format(annualTaxCredits) +"</td>")
                                .append("</tr>")
                                .append("<tr>")
                                    .append("<td style=\"color:#0074f2;\">Calculated PAYE after tax credits</td>")                            
                                    .append("<td class=\"text-right\">" + df.format(PAYEMonthly) +"</td>")
                                    .append("<td class=\"text-right\">" + df.format(PAYEAnnualy) +"</td>")
                                .append("</tr>")
                                .append("<tr>")
                                    .append("<td style=\"color:#0074f2;\">Take home pay</td>")
                                    .append("<td class=\"text-right\">" + df.format(NettMonthly) + "</td>")
                                    .append("<td class=\"text-right\">" + df.format(NettAnnually) +"</td>")
                                .append("</tr>")                    
                            .append("</tbody>")
                        .append("</table> ");
                    } catch (Exception e) {
                        Logger.getLogger(taxCalculation.class.getName()).log(Level.SEVERE, null, e);                 
                    }        
             calculatedResponse = stringBuilder.toString();
        }
    }
     

    
    
    public void returnError(String errorMessage) {
        FacesContext.getCurrentInstance().addMessage("calculatorForm:results-messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, errorMessage));
    } 
           

     public boolean isHasContribution() {
        return hasContribution;
    }
     
    public double getNettMonthly() {
        return NettMonthly;
    }

    public void setNettMonthly(double NettMonthly) {
        this.NettMonthly = NettMonthly;
    }

    public double getNettAnnually() {
        return NettAnnually;
    }

    public void setNettAnnually(double NettAnnually) {
        this.NettAnnually = NettAnnually;
    }

    public void setHasContribution(boolean hasContribution) {
        this.hasContribution = hasContribution;
    }
     
    public String getCalculatedResponse() {
        return calculatedResponse;
    }

    public void setCalculatedResponse(String calculatedResponse) {
        this.calculatedResponse = calculatedResponse;
    }
     public int getSalaryMonthlyOrAnnually() {
        return salaryMonthlyOrAnnually;
    }

    public void setSalaryMonthlyOrAnnually(int salaryMonthlyOrAnnually) {
        this.salaryMonthlyOrAnnually = salaryMonthlyOrAnnually;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public double getMonthlyPAYE() {
        return monthlyPAYE;
    }

    public void setMonthlyPAYE(double monthlyPAYE) {
        this.monthlyPAYE = monthlyPAYE;
    }

    public double getAnnualPAYE() {
        return annualPAYE;
    }

    public void setAnnualPAYE(double annualPAYE) {
        this.annualPAYE = annualPAYE;
    }

    public double getTaxCredits() {
        return taxCredits;
    }

    public void setTaxCredits(double taxCredits) {
        this.taxCredits = taxCredits;
    }

   
    
    public int getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }
    
    public double getPAYEAnnualy() {
        return PAYEAnnualy;
    }

    public void setPAYEAnnualy(double PAYEAnnualy) {
        this.PAYEAnnualy = PAYEAnnualy;
    }

    public double getPAYEMonthly() {
        return PAYEMonthly;
    }

    public void setPAYEMonthly(double PAYEMonthly) {
        this.PAYEMonthly = PAYEMonthly;
    }



    public double getTotalTaxableEarnings() {
        return totalTaxableEarnings;
    }

    public void setTotalTaxableEarnings(double totalTaxableEarnings) {
        this.totalTaxableEarnings = totalTaxableEarnings;
    }
    
 
    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }       

    public double getTaxRebates() {
        return taxRebates;
    }

    public void setTaxRebates(double taxRebates) {
        this.taxRebates = taxRebates;
    }

    public double getTaxThreshold() {
        return taxThreshold;
    }

    public void setTaxThreshold(double taxThreshold) {
        this.taxThreshold = taxThreshold;
    }

    public double getMedicalTaxCredits() {
        return medicalTaxCredits;
    }

    public void setMedicalTaxCredits(double medicalTaxCredits) {
        this.medicalTaxCredits = medicalTaxCredits;
    }
 

    public double getAnnualTaxCredits() {
        return annualTaxCredits;
    }

    public void setAnnualTaxCredits(double annualTaxCredits) {
        this.annualTaxCredits = annualTaxCredits;
    }
    
     public int getNoOfDependants() {
        return noOfDependants;
    }

    public void setNoOfDependants(int noOfDependants) {
        this.noOfDependants = noOfDependants;
    }
    
      

    private static class taxCalculation {

        public taxCalculation() {
        }
    }

    
    
}

//Taxable income = Annual gross salary - Pension / Provident / RAF (limited to 27.5% of salary, limited to R350k) - 20% of travel allowance
//(You are taxed on 80% of the travel allowance in your Gross salary, so we subtract 20% for the calculation of Taxable income.)
//Taxable income = R 240,000.00 - R 0.00 - R 0.00
//Taxable income for the year: R 240,000.00
//Tax you will pay / PAYE (Pay As You Earn) for your age group and income bracket: R 2,709.33  (as per PAYE tables provided by SARS)
//Take home pay = Gross salary - PAYE - UIF
//(UIF / Unemployment Insurance Fund is levied at 1% of your gross income, at most R148.72/month.)
//Take home pay = R 20,000.00 - R 2,709.33 - R 148.72
//Take home pay: R 17,141.95 per month :)