import json
from openai import OpenAI
from enum import Enum

import kotlinInterop


class UserDetails(str, Enum):
    LOCATION_CITY = "LOCATION_CITY"
    LOCATION_COUNTRY = "LOCATION_COUNTRY"
    SMALLHOLDING_SIZE = "SMALLHOLDING_SIZE"
    NAME = "NAME"
    MAIN_CROP = "MAIN_CROP"


def cleanMessage(messageWithoutInformation):
    """cleaned message"""
    return messageWithoutInformation


client = OpenAI(api_key=kotlinInterop.getEnv("OPENAI_API_KEY"))


def removeNewInformation(userMessage, userDetails):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": f"""
                            The user input contains the following information {userDetails}. 
                            Remove parts of the user input which gave information, and pass the cleaned input as the messageWithoutInformation parameter.
                            
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
                    "name": "cleanMessage",
                    "description": "returns the cleaned message",
                    "parameters": {
                        "type": "object",
                        "properties": {

                            "messageWithoutInformation": {
                                "type": "string",
                                "description": "Remove parts of the user input which gave information and pass the cleaned input as the messageWithoutInformation parameter"
                            },
                        },
                        "required": ["messageWithoutInformation"]
                    }
                }
            }
        ],
        tool_choice={
            "type": "function",
            "function": {"name": "cleanMessage"},
        },
        temperature=0.1
    )

    tool_calls = response.choices[0].message.tool_calls
    if tool_calls:
        available_functions = {
            "cleanMessage": cleanMessage,
        }
        for tool_call in tool_calls:
            function_name = tool_call.function.name
            function_to_call = available_functions[function_name]
            function_args = json.loads(tool_call.function.arguments)

            function_response = function_to_call(
                messageWithoutInformation=function_args.get("messageWithoutInformation"),
            )

            return function_response


kotlinInterop.registerFunction('removeNewInformation', removeNewInformation)
kotlinInterop.execute()
