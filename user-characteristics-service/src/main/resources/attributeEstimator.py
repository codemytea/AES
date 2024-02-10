from enum import Enum
import kotlinInterop
from transformers import pipeline




class Gender(str, Enum):
    MALE = "MALE",
    FEMALE = "FEMALE"

class Age(str, Enum):
    ADULT = "ADULT",
    AGED = "AGED"
    YOUNG = "YOUNG"


def ageResultToEnum(label):
    match label:
        case "Adult":
            return Age.ADULT
        case "Aged":
            return Age.AGED
        case _:
            return Age.YOUNG

def genderResultToEnum(label):
    match label:
        case "Male":
            return Gender.MALE
        case _:
            return Gender.FEMALE



def getAgeForMessages(messages):
    pipe = pipeline("text-classification", model="Abderrahim2/bert-finetuned-Age")
    result = pipe(" ".join(messages))[0]
    return ageResultToEnum(result["label"])

def getGenderForMessages(messages):
    pipe = pipeline("text-classification", model="padmajabfrl/Gender-Classification")
    result = pipe(" ".join(messages))[0]
    return genderResultToEnum(result["label"])

kotlinInterop.registerFunction('getAgeForMessages', getAgeForMessages)
kotlinInterop.registerFunction('getGenderForMessages', getGenderForMessages)

kotlinInterop.execute()
