from enum import Enum
from openai import OpenAI
import json
import kotlinInterop



client = OpenAI(api_key=kotlinInterop.getEnv("OPENAI_API_KEY"))


def returnStop(stopRequestInformation, stopRequestNotifications):
    finalArray = []
    if stopRequestInformation is not None:
        finalArray.append([stopRequestInformation, "INFORMATION"])
    if stopRequestNotifications is not None:
        finalArray.append([stopRequestNotifications, "NOTIFICATION"])
    return finalArray


# returns List<Pair<String, Stop>>?
def getStopRequests(userMessage):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": """
                           You want to check if user input contains a request for the system to stop asking them for more information, or stop sending them notifications.
                           If it does, extract any stop request(s) WITH surrounding context and pass them to returnStop as the stopRequestInformation or stopRequestNotifications parameter depending on the type. 
                           Else, just pass null to both.
                           
                           Make sure you check for the word STOP, it is ver important you manage to extract this correctly.


                           Examples:
                           1. "Hello. My name is Jim. Please stop sending me notifications. I think this system is really terrible. But I like that it's intuitive to use" -> "Please stop sending me notifications"
                           2. "Hello. What are you for? I need assistance!! Stop asking for more things about me!!!!!! I need help!" -> "Stop asking for more things about me!!!!!!"
                           """
            },
            {
                "role": "user",
                "content": userMessage
            }
        ],
        tools=[
            {
                "type": "function",
                "function": {
                    "name": "returnStop",
                    "description": "Use this function to extract stop requests from the user input and pass it to the relevant parameter",

                    "parameters": {
                        "type": "object",
                        "properties": {
                            "stopRequestInformation": {
                                "type": "string",
                                "description": "the stop request(s) the user gave to do with information collection. If they have not given any, pass null"
                            },
                            "stopRequestNotifications": {
                                "type": "string",
                                "description": "the stop request(s) the user gave to do with sending notifications. If they have not given any, pass null"
                            },
                        },
                        "required": ["stopRequestInformation", "stopRequestNotifications"]
                    }
                }
            }
        ],
        tool_choice={
            "type": "function",
            "function": {"name": "returnStop"},
        },
        temperature=0
    )

    tool_calls = response.choices[0].message.tool_calls
    if tool_calls:
        available_functions = {
            "returnStop": returnStop,
        }
        for tool_call in tool_calls:
            function_name = tool_call.function.name
            function_to_call = available_functions[function_name]
            function_args = json.loads(tool_call.function.arguments)

            function_response = function_to_call(
                stopRequestInformation=function_args.get("stopRequestInformation"),
                stopRequestNotifications=function_args.get("stopRequestNotifications"),
            )

            return function_response

kotlinInterop.registerFunction('getStopRequests', getStopRequests)
kotlinInterop.execute()
