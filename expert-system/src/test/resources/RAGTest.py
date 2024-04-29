import json
import sys

import kotlinInterop
import os






def test_answer_relevance():
    with open(sys.argv[3]+"/impl_intermediate", 'w') as f:
        f.writelines(["\n", sys.argv[1] + "\n", "test_answer_relevance_impl\n", sys.argv[3] + "\n", sys.argv[4]+"\n"])
    os.system("cd '" + sys.argv[3] + "'&&python3 -m venv venv&&source venv/bin/activate&&pip3 install -U deepeval&&pip3 install -U llama_index&&export OPENAI_API_KEY="+ kotlinInterop.getEnv("OPENAI_API_KEY") + "&&deepeval test run test_RAGTestImpl.py")



test_answer_relevance()


kotlinInterop.registerFunction("test_answer_relevance", test_answer_relevance)


