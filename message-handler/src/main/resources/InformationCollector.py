from openai import OpenAI


class UserDetails(str, Enum):
    LOCATION_CITY = "LOCATION_CITY"
    LOCATION_COUNTRY = "LOCATION_COUNTRY"
    SMALLHOLDING_SIZE = "SMALLHOLDING_SIZE"
    NAME = "NAME"
    MAIN_CROP = "MAIN_CROP"


client = OpenAI()


def collect(userDetails):
    toAppend = ""
    if UserDetails.MAIN_CROP in userDetails:
        print("here")
        toAppend = "Possible main crops are wheat, corn, barley, rice and soy beans."
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": f"""
                            You want to ask the user to provide the following information {userDetails}. 
                            Explain that this is to allow the system to provide more tailored responses to their agricultural questions.
                            
                            {toAppend}
                            """
            },
        ],
        temperature=0.1
    )

    return response.choices[0].message.content


kotlinInterop.registerFunction('collect'
userDetails)
kotlinInterop.execute()
