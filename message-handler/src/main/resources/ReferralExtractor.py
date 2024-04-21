import kotlinInterop

import json
from openai import OpenAI

def extractReferral(referral):
    """passes referral(s) on to the refer a friend scheme"""
    return referral


client = OpenAI(api_key=kotlinInterop.getEnv("OPENAI_API_KEY"))


def getReferral(userMessage):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": """
                           You want to check if user input contains any referrals (refer a friend).
                           If it does, extract the referral(s) WITH surrounding context and pass them to extractReferral as the referral parameter. Else, just pass an empty list.
                           
                           
                           Examples:
                           1. "Hello. My name is Jim. Please stop sending me notifications. I think this system is really good. Please also send my friend Raf messages on 07563267801" -> ["Please also send my friend Raf messages on 07563267801"]
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
                    "name": "extractReferral",
                    "description": "Use this function to extract referral from the user input and pass it to the parameter",

                    "parameters": {
                        "type": "object",
                        "properties": {
                            "referral": {
                                "type": "array",
                                "items": {
                                    "type": "string"
                                },
                                "description": "the referral the user gave. If they have not given referral, pass an empty array."
                            },
                        },
                        "required": ["referral"]
                    }
                }
            }
        ],
        tool_choice={
            "type": "function",
            "function": {"name": "extractReferral"},
        },
        temperature=0
    )

    tool_calls = response.choices[0].message.tool_calls
    if tool_calls:
        available_functions = {
            "extractReferral": extractReferral,
        }
        for tool_call in tool_calls:
            function_name = tool_call.function.name
            function_to_call = available_functions[function_name]
            function_args = json.loads(tool_call.function.arguments)

            function_response = function_to_call(
                referral=function_args.get("referral"),
            )

            return function_response



kotlinInterop.registerFunction('getReferral', getReferral)
kotlinInterop.execute()
