import json
from openai import OpenAI


class UserDetails(str, Enum):
    LOCATION_CITY = "LOCATION_CITY"
    LOCATION_COUNTRY = "LOCATION_COUNTRY"
    SMALLHOLDING_SIZE = "SMALLHOLDING_SIZE"
    NAME = "NAME"
    MAIN_CROP = "MAIN_CROP"


def extractInformation(locationCity, locationCountry, name, smallholdingSize, mainCrop, stopCollecting,
                       messageWithoutInformation):
    """extracts all new given information from message"""
    return {
        "locationCity": locationCity,
        "locationCountry": locationCountry,
        "name": name,
        "smallholdingSize": smallholdingSize,
        "mainCrop": mainCrop,
        "stopCollecting": stopCollecting,
        "messageWithoutInformation": messageWithoutInformation
    }


client = OpenAI()


def secondLine(userMessage, userDetails):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": f"""
                            You want to check if user input contains any of the following information {userDetails}. 
                            If it does extract the information provided and pass it to extractInformation. 
                            Remove parts of the user input which gave information, or details about stop collecting, and pass the cleaned input as the messageWithoutInformation parameter.
                            Else pass None with the full message.
                            
                            If the user has in any way asked you to stop collecting more information about them in the future, pass stopCollecting as True.
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
                    "name": "extractInformation",
                    "description": "saves collected user information",
                    "parameters": {
                        "type": "object",
                        "properties": {
                            "locationCity": {
                                "type": "string",
                                "description": "The city the smallholding is in, eg Swansea"
                            },
                            "locationCountry": {
                                "type": "string",
                                "description": "The country the smallholding is in, eg Wales"
                            },
                            "name": {
                                "type": "string",
                                "description": "The name of the smallholder, eg Jacqueline"
                            },
                            "smallholdingSize": {
                                "type": "string",
                                "description": "The size of the smallholding, eg 5 acres"
                            },
                            "stopCollecting": {
                                "type": "boolean",
                                "description": "If the user has asked you to stop collecting more information"
                            },
                            "mainCrop": {
                                "type": "string",
                                "description": "The main crop or cash crop of the smallholding. Make sure it is the users cash/main crop.",
                                "enum": [
                                    "wheat",
                                    "barley",
                                    "rice"
                                ]
                            },
                            "messageWithoutInformation": {
                                "type": "string",
                                "description": "Remove parts of the user input which gave information and pass the cleaned input as the messageWithoutInformation parameter"
                            },
                        },
                        "required": ["stopCollecting", "messageWithoutInformation"]
                    }
                }
            }
        ],
        tool_choice={
            "type": "function",
            "function": {"name": "extractInformation"},
        },
        temperature=0.1
    )

    tool_calls = response.choices[0].message.tool_calls
    if tool_calls:
        available_functions = {
            "extractInformation": extractInformation,
        }
        for tool_call in tool_calls:
            function_name = tool_call.function.name
            function_to_call = available_functions[function_name]
            function_args = json.loads(tool_call.function.arguments)

            function_response = function_to_call(
                locationCity=function_args.get("locationCity"),
                locationCountry=function_args.get("locationCountry"),
                name=function_args.get("name"),
                smallholdingSize=function_args.get("smallholdingSize"),
                mainCrop=function_args.get("mainCrop"),
                stopCollecting=function_args.get("stopCollecting"),
                messageWithoutInformation=function_args.get("messageWithoutInformation"),
            )

            return function_response


kotlinInterop.registerFunction('secondLine', userMessage, userDetails)
kotlinInterop.execute()
