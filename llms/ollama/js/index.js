//npm install ollama
//npm start
import ollama from 'ollama'

let chatConfig = {
  model: "phi", 
  prompt: "Cite 20 famous people"
}

async function invokeLLM(props) {
  console.log(`-----`)
  console.log(`[${props.model}]: ${props.prompt}`)
  console.log(`-----`)
  try {
    console.log(`Running prompt...`)
    const result = await ollama.generate({
      model: props.model,
      prompt: props.prompt,
    })
    console.log(`${result.response}\n`)
   }
  catch(error) {
    console.log(`Query failed!`)
    console.log(error)
  }
}

invokeLLM(chatConfig)