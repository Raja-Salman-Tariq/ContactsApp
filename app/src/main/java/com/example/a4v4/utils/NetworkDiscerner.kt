package com.example.a4v4.utils

import com.example.a4v4.database.ContactsModel

class NetworkDiscerner(contact  :   String?){

    var type    :   Short

    init{

        val str = if (contact== null)
            ""
        else Regex("[^0-9+]").replace(contact, "")


        if (str.length>13 || str.length < 10)
            type    =   ContactsModel.TYPE_OTHER

        else {
            val substr = str.substring(0, 7)

            type = ContactsModel.TYPE_OTHER

            if (!isUfone(substr))
                if (!isJazz(substr))
                    if (!isZong(substr))
                        if (!isTelenor(substr))
                            type = ContactsModel.TYPE_OTHER
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    private fun isTelenor(substr: String): Boolean {
            if (substr.run{ contains("0340") ||
                        contains("0341") ||
                        contains("0342") ||
                        contains("0343") ||
                        contains("0344") ||
                        contains("0345") ||
                        contains("0346") ||
                        contains("0347") ||
                        contains("0348") ||
                        contains("0349") ||

                        contains("+92340") ||
                        contains("+92341") ||
                        contains("+92342") ||
                        contains("+92343") ||
                        contains("+92344") ||
                        contains("+92345") ||
                        contains("+92346") ||
                        contains("+92347") ||
                        contains("+92348") ||
                        contains("+92349")
                }){
                type    =   ContactsModel.TYPE_TELENOR
                return true
            }
            return false
    }

    /*--------------------------------------------------------------------------------------------*/

    private fun isZong(substr: String): Boolean {
        if (substr.run{ contains("0310") ||
                    contains("0311") ||
                    contains("0312") ||
                    contains("0313") ||
                    contains("0314") ||
                    contains("0315") ||
                    contains("0316") ||
                    contains("0317") ||
                    contains("0318") ||

                    contains("+92310") ||
                    contains("+92311") ||
                    contains("+92312") ||
                    contains("+92313") ||
                    contains("+92314") ||
                    contains("+92315") ||
                    contains("+92316") ||
                    contains("+92317") ||
                    contains("+92318")
            }){
            type    =   ContactsModel.TYPE_ZONG
            return true
        }
        return false
    }

    /*--------------------------------------------------------------------------------------------*/

    private fun isJazz(substr: String): Boolean {
        if (substr.run{ contains("0300") ||
                    contains("0301") ||
                    contains("0302") ||
                    contains("0303") ||
                    contains("0304") ||
                    contains("0305") ||
                    contains("0306") ||
                    contains("0307") ||
                    contains("0308") ||
                    contains("0309") ||

                    contains("+92300") ||
                    contains("+92301") ||
                    contains("+92302") ||
                    contains("+92303") ||
                    contains("+92304") ||
                    contains("+92305") ||
                    contains("+92306") ||
                    contains("+92307") ||
                    contains("+92308") ||
                    contains("+92309") ||

                    contains("0320") ||
                    contains("0321") ||
                    contains("0322") ||
                    contains("0323") ||
                    contains("0324") ||

                    contains("+92320") ||
                    contains("+92321") ||
                    contains("+92322") ||
                    contains("+92323") ||
                    contains("+92324")
            }){
            type    =   ContactsModel.TYPE_JAZZ
            return true
        }
        return false
    }

    /*--------------------------------------------------------------------------------------------*/

    private fun isUfone(substr: String): Boolean {
        if (substr.run{ contains("0330") ||
                    contains("0331") ||
                    contains("0332") ||
                    contains("0333") ||
                    contains("0334") ||
                    contains("0335") ||
                    contains("0336") ||
                    contains("0337") ||

                    contains("+92330") ||
                    contains("+92331") ||
                    contains("+92332") ||
                    contains("+92333") ||
                    contains("+92334") ||
                    contains("+92335") ||
                    contains("+92336") ||
                    contains("+92337")
        }){
            type    =   ContactsModel.TYPE_UFONE
            return true
        }
        return false
    }
}