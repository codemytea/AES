import kotlinInterop
from openai import OpenAI

client = OpenAI(api_key=kotlinInterop.getEnv("OPENAI_API_KEY"))

prompt = """ 
    You are a bot that summarises answers to agricultural questions. You will be given a users' knowledge level 
    on the area the question is about. The knowledge level will be between 0 and 1.
    If the users' knowledge level is 0 DO NOT SUMMARISE AT ALL. If the users knowledge level is 1, summarise 
    as much as you can. Summarise accordingly to any level in between.
 
    
    DO NOT add any new text. Your job is to only rewrite messages. DO NOT OFFER ASSISTANCE. Keep ALL punctuation as is.
    """

def userKnowledgeCompiling(userMessage, knowledgeLevel):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": prompt.format(knowledgeLevel)
            },
            {
                "role": "user",
                "content": "My knowledge level about this is " + str(knowledgeLevel) + " " + userMessage
            },
        ],
        temperature=0
    )

    return response.choices[0].message.content

kotlinInterop.registerFunction('userKnowledgeCompiling', userKnowledgeCompiling)

kotlinInterop.execute()