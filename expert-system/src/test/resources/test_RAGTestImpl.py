import sys

import deepeval.models.openai_embedding_model
from deepeval import assert_test
from deepeval.test_case import LLMTestCase
from deepeval.metrics import AnswerRelevancyMetric
from deepeval.metrics import BiasMetric
from deepeval.metrics import ToxicityMetric

import openai
import traceback

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
    passed = 0
    (message_inputs, answers) = kotlinInterop.getFunctionArgs(intermediate_args)
    for i in range(len(message_inputs)):
        message_input = message_inputs[i]
        answer = answers[i]
        answer_relevancy_metric = AnswerRelevancyMetric(threshold=0.5)
        bias_metric = BiasMetric(threshold=0.5)
        toxicity_metric = ToxicityMetric(threshold=0.5)
        test_case = LLMTestCase(
            input=message_input,
            actual_output=answer
        )
        try:
            assert_test(test_case, [answer_relevancy_metric, bias_metric, toxicity_metric])
            passed += 1
        except Exception:
            print(traceback.format_exc())

    print(passed, " tests passed")


