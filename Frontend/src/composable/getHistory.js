const getHistory = async () => {
  try {
    const res = await fetch('http://localhost:8080/api/history')
    if (res.ok) {
      const historys = await res.json()
      return historys
    } 
    else {
      throw new Error('Error')
    }
  } 
  catch (error) {
    console.log(error)
  }
}
export { getHistory }