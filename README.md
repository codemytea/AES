# AES
An SMS-based Agricultural Extension Service tailored for smallholders in LICs.

This repository contains the code for CS310 and its associated project

## Main Modules (Overview)
 - `sms-services`: Controls the sending and receiving of messages using Gateway API
 - `notification-service`: Controls sending event-driven notifications (`notification-trigger-task`) and temporal notifications (`notification-service-task`)
 - `message-handler`: Deals with incoming messages (through generating correct and relevant responses)
 - `message-compiler`: Deals with outgoing messages (tailoring, compiling, chunking e.t.c.)
 - `expert-system`: Answers agricultural queries using RAG
 - `user-characteristic-service`: Calculates and provides user characteristics for qualitative tailoring
 - `common`: A collection of functionality/entities/models e.t.c. used by multiple modules - reduces code duplication
 - `kotlin-python-interop`: Allows for Python functions (found in the 'resources' folder in 'main' of each module where applicable) to be called and run by Kotlin code in the `kotlin` folder.

## Exploring the Code

If you'd like to have a look through the code, it is strongly recommended you start at `sms-services/src/main/kotlin/com/aes/smsservices/Controllers/SmsController.kt`, as this is the starting point of the entire application. Click through to each function to see the route an incoming message would take as this will provide the most holistic understanding of the system. 

### Queues

In several places, there are 'hops' to other modules through the use of queues - for instance: 

```
fun sendToMessageHandler(sms: Message) { 
      localQueueService.writeItemToQueue("message_handler_queue", sms) 
}
```

To re-enter the code at the correct position, do a global search throughout the project for the correct queue (in this case `message_handler_queue` using command+shift+f in IntelliJ on Mac) and click on the result in the file titled `[NAMEOFQUEUE]ReadService.kt` where `[NAMEOFQUEUE]` is the name of the queue in snake case (in this instance `MessageHandlerQueueReadService.kt`). This is where the item on the queue gets picked up once it is its turn for processing, and therefore where it's most explanatory for you to keep reading from. Once you have finished looking at the code on that end you can 'hop' back to where the item was put on the queue initially to see what happens after processing.

To understand more about how queues work, please navigate to `common/src/main/kotlin/com/aes/common/Queue`.

### Tasks

Several services run as tasks (such as `user-characteristics-service`). Here there is no logical entry point to the code. Instead these tasks are 'scheduled' and run every so often. A good example of this is recalculating a users knowledge of a certain knowledge area - this happens once a night, daily. To have a look at such tasks, search for the annotation `@Scheduled`.

### Python

Python code is called from functions that look like this:

```
@PythonFunction("getTopicOfMessage", "knowledgeClassifier.py")
    fun getTopicOfMessage(messages: List<String>): Topic {
        return execute(::getTopicOfMessage, messages.toTypedArray())
    }
```

To see the corresponding Python code behind the scenes, please visit the `resources` folder (for instance, in `user-characteristic-service`, the folder is located at `user-characteristics-service/src/main/resources`), and find the corresponding file (second parameter of the `@PythonFunction annotation`, in this case `knowledgeKlassifier.py`). The function being called is the first argument of the annotation.

To understand more about how the interop works, please have a look at the `kotlin-python-interop` module.


## Set Up

Each module has unique set up and running instructions, but here are some general ones first.

1) Unzip the code into an IDE of your choosing (IntelliJ IDEA STRONGLY recommended (https://www.jetbrains.com/idea/ - community version is free), else you may have to install many dependencies. IntelliJ IDEA is the only IDE this project has been tested on).
2) If you do not have Python installed on your machine, install Python (latest version).
3) Create an OpenAI account (LLM used throughout multiple modules) and create an API key (https://platform.openai.com/settings/profile?tab=api-keys and SAVE it - it disappears once you create it). Then follow the instructions on the website (windows and mac instructions given https://help.openai.com/en/articles/5112595-best-practices-for-api-key-safety). The Open AI version has been set to 3.5-turbo throughout the codebase so you can run it without paying for version 4.0, but please note, in order to get accuracy benchmarks discussed in dissertation, you will need to use the paid 4.0 version as that is what this project was developed with. You will also need to pip install openai. The following tutorial also explains set up - https://platform.openai.com/docs/quickstart?context=python.
4) Run the tests in ExpertSystemApplicationTests to pre-populate your database with some data necessary for the Expert System to run.

Note, all API keys must be put into a credentials.properties file, copying the template provided in credentials.properties.example file. You database settings must also be added to each applications.properties file, following the instructions provided in them.

### sms-services

To run sms-services, you will need a UK shortcode to receive messages from the system and a GatewayAPI key. 

1) Navigate to https://gatewayapi.com/ and create an account. 
2) Create an API Key (Paid)
3) Get in contact with Customer Support and request a UK shortcode. When this project was set up, the charge for this was £10 set up fee and £10 per month their after.
4) Using the shortcode and API key replace `[UKSHORTCODE]` and `[GATEWAYAPIKEY]` with your information. You will also need to create a URL to receive your requests at and add it to the webhook section of the GatewayAPI portal. 

### message-handler

This service uses a custom trained NER for information extraction. The python code for the NER trainer has been provided in the `message-handler` module under `resources`. Please open the `NERtrainer.txt` and copy it into a python file in a new project on your machine. Also copy `annotations.json` and put it in a folder called `Data`. Your project structure MUST look as follows:

```
NERTrainerProject/
----NERTrainer.py
----Data/
--------annotations.json
```

Note that this assumes that your current working directory (cwd) is the NERTrainerProject folder.

To run the NER trainer, you will need to import the SpaCy library (`pip install spacy`) and sklearn (`pip install -U scikit-learn`). 
Then run the following commands:

- `python -m spacy init fill-config ./Data/base_config.cfg ./Data/config.cfg`
- `python -m spacy train ./Data/config.cfg  --output ./Data  --paths.train ./Data/train_data.spacy  --paths.dev ./Data/test_data.spacy`


Once the model has been trained, copy the folder `model-best` from the output and put it in the `data` folder under `resources` under `main` in the `message-handler` module (`message-handler/src/main/resources/data`).


### user-characteristic-service

This service uses the Hugging Face `facebook/bart-large-mnli`, `Abderrahim2/bert-finetuned-Age` and `padmajabfrl/Gender-Classification` models. To run these models, you will need to import `transformers` using the instructions provided here https://huggingface.co/docs/transformers/en/installation.

### expert-system

1) Please follow the quick-start set up docs here https://docs.llamaindex.ai/en/stable/getting_started/installation/ to start.
2) You will then need to set up TrefleAPI (replace `[TREFLEAPIKEY]` with your key in the code) - https://trefle.io/

3) [ISAAC]

## Running

Once everything is set up, the project can be run by starting all the services. Please make sure you have nothing running from port 8080 to 8090. If you do and you can't stop them, navigate to the relevant `application.properties` (for instance, for the `sms-service` this is located at `sms-services/src/main/resources/application.properties`) and change:

```
server.port=8080
```

To a port of your choosing.

You can then send a message to your UK shortcode using a mobile device and view the responses. If you'd like to see Scheduled Tasks in action, you can change the `cron` to be more frequent.

## License 

Copyright (C) Dobreva-Skevington Ltd. - All Rights Reserved. 
Unauthorized copying of any aspect of this codebase, for any use, via any medium is strictly prohibited. 
This codebase has solely been shared for the marking of the CS310 Final Report Coursework (University of Warwick) and must not be used, or stored, for any other purpose.
Proprietary and confidential.
Written by Jacqueline Dobreva-Skevington <jacqueline.dobreva@gmail.com>, April 2024.