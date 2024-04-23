import json
from openai import OpenAI

import kotlinInterop



def retrievedSentences(context):
    """cleaned message"""
    return context


client = OpenAI(api_key=kotlinInterop.getEnv("OPENAI_API_KEY"))


def removeNewInformation(userMessage, userDetails):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": f"""
                            The user input contains the following information {userDetails}. 
                            Get the parts of the user input which gave information, and pass that list to the 'context' parameter, separately as a list. Else, just pass an empty list.
                           
                           
                           Examples:
                           1. "Hello. My name is Jim. Please stop sending me notifications. I think this system is really terrible. But I like that it's intuitive to use. My smallholding is 5 acres" -> ["My name is Jim", "My smallholding is 5 acres"]
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
                    "name": "retrievedSentences",
                    "description": "Get the parts of the user input which gave information",
                    "parameters": {
                        "type": "object",
                        "properties": {
                            "context": {
                                "type": "array",
                                "items": {
                                    "type": "string"
                                },
                                "description": "Get the parts of the user input which gave information, and pass that list to the 'context' parameter. If the user has provided one piece of information, pass an array with one value - that info."
                            },
                        },
                        "required": ["context"]
                    }
                }
            }
        ],
        tool_choice={
            "type": "function",
            "function": {"name": "retrievedSentences"},
        },
        temperature=0.1
    )

    tool_calls = response.choices[0].message.tool_calls
    if tool_calls:
        available_functions = {
            "retrievedSentences": retrievedSentences,
        }
        for tool_call in tool_calls:
            function_name = tool_call.function.name
            function_to_call = available_functions[function_name]
            function_args = json.loads(tool_call.function.arguments)

            function_response = function_to_call(
                context=function_args.get("context"),
            )

            return function_response


kotlinInterop.registerFunction('removeNewInformation', removeNewInformation)
kotlinInterop.execute()
