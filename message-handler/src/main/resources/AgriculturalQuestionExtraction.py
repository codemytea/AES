import kotlinInterop

import json
from openai import OpenAI

def extractQuestion(questions):
    """passes question(s) on to agricultural question answerer"""
    return (questions)


client = OpenAI(api_key=kotlinInterop.getEnv("OPENAI_API_KEY"))


def getQuestions(userMessage):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": """
                           You want to check if user input contains an agricultural question. There could be more than one.
                           If it does, extract the question(s) WITH surrounding context and pass them to extractQuestion as the questions parameter. Else, just pass an empty list.
                           
                           DO NOT extract feedback about the system, or information about the user or their smallholding.
                           
                           
                           Examples:
                           1. "Hello. I would like know how to sell corn please, thank you. also, when do I plant beets? Furthermore, it's been raining lot's recently, is it still the best time to sow jerusalem artichokes?" -> ["I would like to know how to sell corn?", "When do I plant beets?", "It's been raining lot's recently, is it still the best time to sow jerusalem artichokes?"]
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
                    "name": "extractQuestion",
                    "description": "Use this function to extract agricultural questions from the user input and pass it to the parameter",

                    "parameters": {
                        "type": "object",
                        "properties": {
                            "questions": {
                                "type": "array",
                                "items": {
                                    "type": "string"
                                },
                                "description": "the agricultural question(s) the user asked. If they have not asked an agricultural question, pass an empty array. If the user has asked one question. Pass an array with one value - that question."
                            },
                        },
                        "required": ["questions"]
                    }
                }
            }
        ],
        tool_choice={
            "type": "function",
            "function": {"name": "extractQuestion"},
        },
        temperature=0
    )

    tool_calls = response.choices[0].message.tool_calls
    if tool_calls:
        available_functions = {
            "extractQuestion": extractQuestion,
        }
        for tool_call in tool_calls:
            function_name = tool_call.function.name
            function_to_call = available_functions[function_name]
            function_args = json.loads(tool_call.function.arguments)

            function_response = function_to_call(
                questions=function_args.get("questions"),
            )

            return function_response



kotlinInterop.registerFunction('getQuestions', getQuestions)
kotlinInterop.execute()
