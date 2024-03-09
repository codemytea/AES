import kotlinInterop
from openai import OpenAI

client = OpenAI()

prompt = """ 
    You are a bot that re-writes suggestions (so users are more susceptible to them) by mirroring their characteristics.
    Given a set of characteristics (age, gender, literacy level), you want to rewrite the user input to that the input. 
    Put most emphasis on user literacy level. The age, then gender.
    
    User literacy is rated from 0 to 100, where 0 is extremely low (user cannot understand english) and 100 is extremely high (user can understand anything).
    
    The user characteristics are as follows:
    User Literacy: {}
    User Gender: {}
    User Age: {}
    
    DO NOT add any new text. Your job is to only rewrite messages. DO NOT OFFER ASSISTANCE. Keep ALL punctuation as is.
    """

def userCharacteristicCompiling(userMessage, literacyLevel, gender, age):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": prompt.format(literacyLevel, gender, age)
            },
            {
                "role": "user",
                "content": userMessage
            },
        ],
        temperature=0
    )

    return response.choices[0].message.content

kotlinInterop.registerFunction('userCharacteristicCompiling', userCharacteristicCompiling)

kotlinInterop.execute()