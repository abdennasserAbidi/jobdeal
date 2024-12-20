package com.example.myjob.domain.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//import com.google.gson.annotations.SerialName

@Serializable
@Entity(tableName = "exchanges")
class ExchangeRates {

    @PrimaryKey
    var id: Int = 0

    @SerialName("success")
    var success: Boolean? = false

    @SerialName("timestamp")
    var timestamp: Double? = 0.0

    @SerialName("base")
    var base: String? = ""

    @SerialName("date")
    var date: String? = ""

    @SerialName("rates")
    @Ignore
    var rates: Rates? = Rates()
}

@Entity(tableName = "rates")
@Serializable
class Rates {

    @PrimaryKey(autoGenerate = true)
    var idrates: Int? = 0

    @SerialName("AED")
    var usdAED: Double? = 0.0

    @SerialName("AFN")
    var usdAFN: Double? = 0.0

    @SerialName("ALL")
    var usdALL: Double? = 0.0

    @SerialName("AMD")
    var usdAMD: Double? = 0.0

    @SerialName("ANG")
    var usdANG: Double? = 0.0

    @SerialName("AOA")
    var usdAOA: Double? = 0.0

    @SerialName("ARS")
    var usdARS: Double? = 0.0

    @SerialName("AUD")
    var usdAUD: Double? = 0.0

    @SerialName("AWG")
    var usdAWG: Double? = 0.0

    @SerialName("AZN")
    var usdAZN: Double? = 0.0

    @SerialName("BAM")
    var usdBAM: Double? = 0.0

    @SerialName("BBD")
    var usdBBD: Double? = 0.0

    @SerialName("BDT")
    var usdBDT: Double? = 0.0

    @SerialName("BGN")
    var usdBGN: Double? = 0.0

    @SerialName("BHD")
    var usdBHD: Double? = 0.0

    @SerialName("BIF")
    var usdBIF: Double? = 0.0

    @SerialName("BMD")
    var usdBMD: Double? = 0.0

    @SerialName("BND")
    var usdBND: Double? = 0.0

    @SerialName("BOB")
    var usdBOB: Double? = 0.0

    @SerialName("BRL")
    var usdBRL: Double? = 0.0

    @SerialName("BSD")
    var usdBSD: Double? = 0.0

    @SerialName("BTC")
    var usdBTC: Double? = 0.0

    @SerialName("BTN")
    var usdBTN: Double? = 0.0

    @SerialName("BWP")
    var usdBWP: Double? = 0.0

    @SerialName("BYN")
    var usdBYN: Double? = 0.0

    @SerialName("BZD")
    var usdBZD: Double? = 0.0

    @SerialName("CAD")
    var usdCAD: Double? = 0.0

    @SerialName("CDF")
    var usdCDF: Double? = 0.0

    @SerialName("CHF")
    var usdCHF: Double? = 0.0

    @SerialName("CLP")
    var usdCLP: Double? = 0.0

    @SerialName("CNY")
    var usdCNY: Double? = 0.0

    @SerialName("COP")
    var usdCOP: Double? = 0.0

    @SerialName("CRC")
    var usdCRC: Double? = 0.0

    @SerialName("CUP")
    var usdCUP: Double? = 0.0

    @SerialName("CVE")
    var usdCVE: Double? = 0.0

    @SerialName("CZK")
    var usdCZK: Double? = 0.0

    @SerialName("DJF")
    var usdDJF: Double? = 0.0

    @SerialName("DKK")
    var usdDKK: Double? = 0.0

    @SerialName("DOP")
    var usdDOP: Double? = 0.0

    @SerialName("DZD")
    var usdDZD: Double? = 0.0

    @SerialName("EGP")
    var usdEGP: Double? = 0.0

    @SerialName("ERN")
    var usdERN: Double? = 0.0

    @SerialName("ETB")
    var usdETB: Double? = 0.0

    @SerialName("EUR")
    var usdEUR: Double? = 0.0

    @SerialName("FJD")
    var usdFJD: Double? = 0.0

    @SerialName("FKP")
    var usdFKP: Double? = 0.0

    @SerialName("GBP")
    var usdGBP: Double? = 0.0

    @SerialName("GEL")
    var usdGEL: Double? = 0.0

    @SerialName("GGP")
    var usdGGP: Double? = 0.0

    @SerialName("GHS")
    var usdGHS: Double? = 0.0

    @SerialName("GIP")
    var usdGIP: Double? = 0.0

    @SerialName("GMD")
    var usdGMD: Double? = 0.0

    @SerialName("GNF")
    var usdGNF: Double? = 0.0

    @SerialName("GTQ")
    var usdGTQ: Double? = 0.0

    @SerialName("GYD")
    var usdGYD: Double? = 0.0

    @SerialName("HKD")
    var usdHKD: Double? = 0.0

    @SerialName("HNL")
    var usdHNL: Double? = 0.0

    @SerialName("HRK")
    var usdHRK: Double? = 0.0

    @SerialName("HTG")
    var usdHTG: Double? = 0.0

    @SerialName("HUF")
    var usdHUF: Double? = 0.0

    @SerialName("IDR")
    var usdIDR: Double? = 0.0

    @SerialName("ILS")
    var usdILS: Double? = 0.0

    @SerialName("IMP")
    var usdIMP: Double? = 0.0

    @SerialName("INR")
    var usdINR: Double? = 0.0

    @SerialName("IQD")
    var usdIQD: Double? = 0.0

    @SerialName("IRR")
    var usdIRR: Double? = 0.0

    @SerialName("ISK")
    var usdISK: Double? = 0.0

    @SerialName("JEP")
    var usdJEP: Double? = 0.0

    @SerialName("JMD")
    var usdJMD: Double? = 0.0

    @SerialName("JOD")
    var usdJOD: Double? = 0.0

    @SerialName("JPY")
    var usdJPY: Double? = 0.0

    @SerialName("KES")
    var usdKes: Double? = 0.0

    @SerialName("KGS")
    var usdKGS: Double? = 0.0

    @SerialName("KHR")
    var usdKHR: Double? = 0.0

    @SerialName("KMF")
    var usdKMF: Double? = 0.0

    @SerialName("KPW")
    var usdKPW: Double? = 0.0

    @SerialName("KRW")
    var usdKRW: Double? = 0.0

    @SerialName("KWD")
    var usdKWD: Double? = 0.0

    @SerialName("KYD")
    var usdKYD: Double? = 0.0

    @SerialName("KZT")
    var usdKZT: Double? = 0.0

    @SerialName("LAK")
    var usdLAK: Double? = 0.0

    @SerialName("LBP")
    var usdLBP: Double? = 0.0

    @SerialName("LKR")
    var usdLKR: Double? = 0.0

    @SerialName("LRD")
    var usdLRD: Double? = 0.0

    @SerialName("LSL")
    var usdLSL: Double? = 0.0

    @SerialName("LYD")
    var usdLYD: Double? = 0.0

    @SerialName("MAD")
    var usdMAD: Double? = 0.0

    @SerialName("MDL")
    var usdMDL: Double? = 0.0

    @SerialName("MGA")
    var usdMGA: Double? = 0.0

    @SerialName("MKD")
    var usdMKD: Double? = 0.0

    @SerialName("MMK")
    var usdMMK: Double? = 0.0

    @SerialName("MNT")
    var usdMNT: Double? = 0.0

    @SerialName("MOP")
    var usdMOP: Double? = 0.0

    @SerialName("MRO")
    var usdMRO: Double? = 0.0

    @SerialName("MUR")
    var usdMUR: Double? = 0.0

    @SerialName("MVR")
    var usdMVR: Double? = 0.0

    @SerialName("MWK")
    var usdMWK: Double? = 0.0

    @SerialName("MXN")
    var usdMXN: Double? = 0.0

    @SerialName("MYR")
    var usdMYR: Double? = 0.0

    @SerialName("MZN")
    var usdMZN: Double? = 0.0

    @SerialName("NAD")
    var usdNAD: Double? = 0.0

    @SerialName("NGN")
    var usdNGN: Double? = 0.0

    @SerialName("NIO")
    var usdNIO: Double? = 0.0

    @SerialName("NOK")
    var usdNOK: Double? = 0.0

    @SerialName("NPR")
    var usdNPR: Double? = 0.0

    @SerialName("NZD")
    var usdNZD: Double? = 0.0

    @SerialName("OMR")
    var usdOMR: Double? = 0.0

    @SerialName("PAB")
    var usdPAB: Double? = 0.0

    @SerialName("PEN")
    var usdPEN: Double? = 0.0

    @SerialName("PGK")
    var usdPGK: Double? = 0.0

    @SerialName("PHP")
    var usdPHP: Double? = 0.0

    @SerialName("PKR")
    var usdPKR: Double? = 0.0

    @SerialName("PLN")
    var usdPLN: Double? = 0.0

    @SerialName("PYG")
    var usdPYG: Double? = 0.0

    @SerialName("QAR")
    var usdQAR: Double? = 0.0

    @SerialName("RON")
    var usdRON: Double? = 0.0

    @SerialName("RSD")
    var usdRSD: Double? = 0.0

    @SerialName("RUB")
    var usdRUB: Double? = 0.0

    @SerialName("RWF")
    var usdRWF: Double? = 0.0

    @SerialName("SAR")
    var usdSAR: Double? = 0.0

    @SerialName("SBD")
    var usdSBD: Double? = 0.0

    @SerialName("SCR")
    var usdSCR: Double? = 0.0

    @SerialName("SDG")
    var usdSDG: Double? = 0.0

    @SerialName("SEK")
    var usdSEK: Double? = 0.0

    @SerialName("SGD")
    var usdSGD: Double? = 0.0

    @SerialName("SHP")
    var usdSHP: Double? = 0.0

    @SerialName("SLL")
    var usdSLL: Double? = 0.0

    @SerialName("SOS")
    var usdSOS: Double? = 0.0

    @SerialName("SRD")
    var usdSRD: Double? = 0.0

    @SerialName("SSP")
    var usdSSP: Double? = 0.0

    @SerialName("STN")
    var usdSTN: Double? = 0.0

    @SerialName("SVC")
    var usdSVC: Double? = 0.0

    @SerialName("SYP")
    var usdSYP: Double? = 0.0

    @SerialName("SZL")
    var usdSZL: Double? = 0.0

    @SerialName("THB")
    var usdTHB: Double? = 0.0

    @SerialName("TJS")
    var usdTJS: Double? = 0.0

    @SerialName("TMT")
    var usdTMT: Double? = 0.0

    @SerialName("TND")
    var usdTND: Double? = 0.0

    @SerialName("TOP")
    var usdTOP: Double? = 0.0

    @SerialName("TRY")
    var usdTRY: Double? = 0.0

    @SerialName("TTD")
    var usdTTD: Double? = 0.0

    @SerialName("TWD")
    var usdTWD: Double? = 0.0

    @SerialName("TZS")
    var usdTZS: Double? = 0.0

    @SerialName("UAH")
    var usdUAH: Double? = 0.0

    @SerialName("UGX")
    var usdUGX: Double? = 0.0

    @SerialName("USD")
    var usdUSD: Double? = 0.0

    @SerialName("UYU")
    var usdUYU: Double? = 0.0

    @SerialName("UZS")
    var usdUZS: Double? = 0.0

    @SerialName("VEF")
    var usdVEF: Double? = 0.0

    @SerialName("VND")
    var usdVND: Double? = 0.0

    @SerialName("VUV")
    var usdVUV: Double? = 0.0

    @SerialName("WST")
    var usdWST: Double? = 0.0

    @SerialName("XAF")
    var usdXAF: Double? = 0.0

    @SerialName("XAG")
    var usdXAG: Double? = 0.0

    @SerialName("XAU")
    var usdXAU: Double? = 0.0

    @SerialName("XCD")
    var usdXCD: Double? = 0.0

    @SerialName("XOF")
    var usdXOF: Double? = 0.0

    @SerialName("XPF")
    var usdXPF: Double? = 0.0

    @SerialName("YER")
    var usdYER: Double? = 0.0

    @SerialName("ZAR")
    var usdZAR: Double? = 0.0

    @SerialName("ZMW")
    var usdZMW: Double? = 0.0

    @SerialName("ZWL")
    var usdZWL: Double? = 0.0

    fun buildList(): MutableList<Currency> {
        val currencies = mutableListOf<Currency>()
        val declaredFields = javaClass.declaredFields
        for (i in 0..declaredFields.size-2) {
            if (declaredFields[i].name != "idrates") {
                val field = javaClass.getDeclaredField(declaredFields[i].name)
                val serilizedName = field.getAnnotation(SerialName::class.java).value
                val currencyCode = declaredFields[i].name
                val exchangeRate = declaredFields[i].get(this) as Double
                currencies.add(Currency(serilizedName, rate = exchangeRate))
            }
        }
        return currencies
    }
}