package com.aes.expertsystem.Services

import com.aes.expertsystem.Python.ExpertSystem
import org.springframework.stereotype.Service

@Service
class ExpertSystemService(
    private val expertSystem: ExpertSystem
) {

    //HI ISAAC ! I love you!!
    fun getAgriculturalAnswer(message : String): String {

        return expertSystem.getAnswer(message)

        //takes in an agricultural question such as "When do I plant corn?"
        //will never take in any more input so don't worry about removing superfluous text etc

        //THis is the kotlin side - you'll need to do all of the below in Python

        //uses OpenAI


        //start off with data connectors - ingests existing data (eg database, function calls and documents)
        //use data indices to then structure the data in a performant way for openai
        //make sure that you use CHAT engine, not query engine or any other type (this makes it context aware and therefore capable of back and forth convos)
            //but for this, you'll need to figure out how it'll deal with multiple users - maybe pass it convo history as datasource, in which case you won't need to use teh chat engine



    }
}

