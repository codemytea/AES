import sys

import deepeval.models.openai_embedding_model
from deepeval import assert_test
from deepeval.test_case import LLMTestCase
from deepeval.metrics import AnswerRelevancyMetric

import openai

import kotlinInterop

def e(a, b):
    pass

intermediate_args = []
with open("impl_intermediate") as f:
    intermediate_args = [s[:-1] for s in f.readlines()]
kotlinInterop.registerFunction("test_answer_relevance_impl", e)
kotlinInterop.execute(intermediate_args)

openai.api_key = kotlinInterop.getEnv("OPENAI_API_KEY", intermediate_args)


def test_answer_relevance_impl():
    (message_inputs, answers) = kotlinInterop.getFunctionArgs(intermediate_args)
    for i in range(len(message_inputs)):
        message_input = message_inputs[i]
        answer = answers[i]
        answer_relevancy_metric = AnswerRelevancyMetric(threshold=0.5)
        test_case = LLMTestCase(
            input=message_input,
            actual_output=answer
        )
        assert_test(test_case, [answer_relevancy_metric])


