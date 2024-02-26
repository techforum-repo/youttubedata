#pip install ollama
#python ollama_phi_python.py

import ollama

result = ollama.generate(model='phi', prompt='Cite 20 famous people')
print(result['response'])