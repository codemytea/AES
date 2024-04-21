import kotlinInterop

import json
from openai import OpenAI

def extractFeedback(feedback):
    """passes question(s) on to agricultural question answerer"""
    return (feedback)


client = OpenAI(api_key=kotlinInterop.getEnv("OPENAI_API_KEY"))


def getFeedback(userMessage):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": """
                           You want to check if user input contains any system feedback.
                           If it does, extract the feedback(s) WITH surrounding context and pass them to extractFeedback as the feedback parameter. Else, just pass an empty list.
                           
                           
                           Examples:
                           1. "Hello. My name is Jim. Please stop sending me notifications. I think this system is really terrible. But I like that it's intuitive to use" -> ["I think this system is really terrible. But I like that it's intuitive to use"]
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
                    "name": "extractFeedback",
                    "description": "Use this function to extract feedback from the user input and pass it to the parameter",

                    "parameters": {
                        "type": "object",
                        "properties": {
                            "feedback": {
                                "type": "array",
                                "items": {
                                    "type": "string"
                                },
                                "description": "the feedback the user gave. If they have not given feedback, pass an empty array."
                            },
                        },
                        "required": ["feedback"]
                    }
                }
            }
        ],
        tool_choice={
            "type": "function",
            "function": {"name": "extractFeedback"},
        },
        temperature=0
    )

    tool_calls = response.choices[0].message.tool_calls
    if tool_calls:
        available_functions = {
            "extractFeedback": extractFeedback,
        }
        for tool_call in tool_calls:
            function_name = tool_call.function.name
            function_to_call = available_functions[function_name]
            function_args = json.loads(tool_call.function.arguments)

            function_response = function_to_call(
                feedback=function_args.get("feedback"),
            )

            return function_response



kotlinInterop.registerFunction('getFeedback', getFeedback)
kotlinInterop.execute()
