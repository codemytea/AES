from openai import OpenAI
import json

client = OpenAI()

def extractQuestion(question, messageWithoutQuestion):
    """passes question(s) on to agricultural question answerer"""
    return json.dumps({"question": question, "messageWithoutQuestion": messageWithoutQuestion,})


client = OpenAI()


def firstLine(userMessage):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": """
                           You want to check if user input contains an agricultural question. There could be more than one.
                           If it does extract the question(s) and pass it to extractQuestion with the left over message as well. 
                           Else pass None with the full message
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
                    "description": "Use this function if the user has asked agricultural question(s)",

                    "parameters": {
                        "type": "object",
                        "properties": {
                            "question": {
                                "type": "string",
                                "description": "the agricultural question(s) the user asked. If they have not asked an agricultural question, pass in None"
                            },
                            "messageWithoutQuestion": {
                                "type": "string",
                                "description": "the original user message with the question removed. If there was no question, pass in the original user message"
                            }
                        },
                        "required": ["userMessage"]
                    }
                }
            }
        ],
        tool_choice={
            "type": "function",
            "function": {"name": "extractQuestion"},
        },
        temperature=0.1
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
                question=function_args.get("question"),
                messageWithoutQuestion=function_args.get("messageWithoutQuestion"),
            )

            return function_response


kotlinInterop.registerFunction('firstLine', userMessage)
kotlinInterop.execute()