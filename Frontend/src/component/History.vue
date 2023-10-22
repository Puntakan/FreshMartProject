<script setup>
import { ref, onMounted } from 'vue'
import cashierEdit from './Cashier.vue'
import trash from './icon/Trash.vue'
import edit from './icon/Edit.vue'
import { getHistory } from '../composable/getHistory.js'

const historys = ref([])

onMounted(async () => {
    historys.value = await getHistory()
})

const deleteHistory = async (deleteId) => {
    try {
        const res = await fetch(`http://localhost:8080/api/history/${deleteId}`, {
            method: 'DELETE'
        })
        if (res.ok) {
            historys.value = historys.value.filter((his) => {
                return his.id !== deleteId
            })
        }
        else throw new Error('Oops, sorry cannot delete')
    }
    catch (err) {
        console.log(err)
    }
}

const currentComponent = ref('historyComponent')
const setCurrentComponent = (curComp) => {
    currentComponent.value = curComp
}

const editHis = ref(undefined)
const setEditMode = (hisEdit) => {
    editHis.value = hisEdit

    setCurrentComponent('CashierComponent')
}

const editHistory = async (updatedHistory) => {
    

    console.log('Customer Type:', updatedHistory.customerType);
    
    if (Array.isArray(updatedHistory.numList)) {
        updatedHistory.numList = JSON.stringify(updatedHistory.numList);
    }

    try {
        const res = await fetch(`http://localhost:8080/api/history/${updatedHistory.id}`, {
            method: 'PUT',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify({
                numList: updatedHistory.numList,
                customerType: updatedHistory.customerType,
                discount: updatedHistory.discount,
                total: updatedHistory.total
            })
        });

        if (res.status === 200) {
            const changed = await res.json();
            console.log(changed);

            historys.value = historys.value.map((historyValue) => {
                if (historyValue.id === changed.id) {
                    historyValue.numList = changed.numList;
                    historyValue.dateTime = changed.dateTime;
                    historyValue.customerType = changed.customerType;
                    historyValue.discount = changed.discount;
                    historyValue.total = changed.total;
                }
                return historyValue;
            });

            setCurrentComponent('historyComponent');
            editHis.value = undefined;
        } else {
            throw new Error('Oops, sorry cannot edit');
        }
    } catch (err) {
        console.log(err);
    }
}

const formatDateTime = (dateTimeString) => {
    const options = {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true,
    };

    const date = new Date(dateTimeString);
    
    date.setHours(date.getHours() - 7);

    const formattedDateTime = date.toLocaleString('en-US', options).replace(',', '');
    
    return formattedDateTime;
}

</script>

<template>
    <cashierEdit v-if="currentComponent === 'CashierComponent'" :history="editHis" @edit="editHistory" />
    <div v-if="currentComponent === 'historyComponent'" class="h-5/6">
        <div class="Title mt-6 mb-6">
            <div class="mx-40">
                Purchase History
            </div>
        </div>

        <div class="Table">
            <div class="h-full overflow-auto">
                <div class="Column">
                    <div class="col-span-1 justify-self-center"></div>
                    <div class="col-span-2 justify-self-center">Date</div>
                    <div class="col-span-2 justify-self-center">Customers</div>
                    <div class="col-span-2 justify-self-center">Discount</div>
                    <div class="col-span-2 justify-self-center">Purchase</div>
                    <div class="col-span-1 justify-self-center"></div>
                </div>
                <div class="Rows" v-for="history in historys">
                    <button class="justify-self-center">
                        <trash class="col-span-1 " @click="deleteHistory(history.id)" />
                    </button>
                    <div class="col-span-2 justify-self-center text-black">{{ formatDateTime(history.dateTime) }}</div>
                    <div class="col-span-2 justify-self-center text-black">{{ history.customerType }}</div>
                    <div class="col-span-2 justify-self-center text-red-600">{{ history.discount }} %</div>
                    <div class="col-span-2 justify-self-center text-black">{{ history.total }}</div>
                    <button class="justify-self-center">
                        <edit class="col-span-1" @click="setEditMode(history)" />
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
* {
    font-family: 'Kanit', sans-serif;

}

.Title {
    width: 100%;
    height: 4rem;
    display: flex;
    align-items: center;
    font-size: 40px;
    font-weight: 700;
    color: #ffffff;
}

.Rows {
    display: grid;
    grid-template-columns: repeat(10, 1fr);
    text-align: center;
    padding: 25px;
    border: 1px solid #acacac3d;
    margin: 30px;
    border-radius: 15px;
    box-shadow: 0px 5px 8px rgba(0, 0, 0, 0.1);
    background-color: rgba(255, 255, 255, 0.9);
    font-weight: 400;
}

.Column {
    display: grid;
    grid-template-columns: repeat(10, 1fr);
    gap: 0.5rem;
    color: #775d26;
    font-weight: 700;
    font-size: 1.25rem;
    margin-top: 1.25rem;
}

.Table {
    width: 80%;
    height: 80%;
    margin: 0 auto;
    border: 1px solid #acacac3d;
    border-radius: 1.5rem;
    background-color: rgba(255, 255, 255, 0.6);
    box-shadow: 0px 10px 15px rgba(0, 0, 0, 0.15);
    backdrop-filter: blur(10px);
}</style>
