<script setup>
import { ref, onMounted } from 'vue'
import trash from './icon/Trash.vue'
import editIcon from './icon/Edit.vue';

const emits = defineEmits(['edit'])



const props = defineProps({
    history: {
        type: Object,
    }
})

const updated = ref({})

onMounted(() => {
    // Add mode 
    if (props.history === undefined) {
        updated.value = {
            numList: [],  // กำหนด numList เป็นอาร์เรย์เปล่า
            dateTime: "",
            customerType: "Guest", // ตั้งค่าเริ่มต้นเป็น Guest
            subTotal: 0,
            discount: 0, // ตั้งค่าเริ่มต้นเป็น 0
            total: 0,
        }
    }
    // Edit mode
    else {
    // เปลี่ยนประเภทของ numList จาก String เป็นอาร์เรย์ของจำนวนเต็ม
    updated.value = props.history;
    updated.value.numList = JSON.parse(updated.value.numList);


    console.log(props)
    console.log(emits)
}
})

const addedNumList = ref([]);


const getPercentBox = (box) => {
    const subTotalValue = updated.value.subTotal
    const customerValue = updated.value.customerType
    switch (box) {
        case 1:
            return subTotalValue >= 5000 && subTotalValue < 10000 && customerValue === 'Member' ? 'bg-green-500 text-black' : 'bg-gray-300 text-gray-400'
        case 2:
            return subTotalValue >= 10000 && subTotalValue < 20000 && customerValue === 'Member' ? 'bg-green-500 text-black' : 'bg-gray-300 text-gray-400'
        case 3:
            return subTotalValue >= 20000 && subTotalValue < 30000 && customerValue === 'Member' ? 'bg-green-500 text-black' : 'bg-gray-300 text-gray-400'
        case 4:
            return subTotalValue >= 30000 && customerValue === 'Member' ? 'bg-green-500 text-black' : 'bg-gray-300 text-gray-400'
        default:
            return 'bg-gray-300'
    }
}

const numberInput = ref('');
const addNumList = () => {
    updated.value.numList.push(Number(numberInput.value));
    numberInput.value = '';

    addedNumList.value.push(updated.value.numList);


};

const deleteItemList = (index) => {
    updated.value.numList.splice(index, 1);
}

const subTotal = () => {
    let numList = updated.value.numList || []; // Initialize as an empty array if numList is undefined
    if (!Array.isArray(numList)) {
        // If numList is not an array, try to convert it to an array
        try {
            numList = JSON.parse(numList);
        } catch (error) {
            console.error("Error parsing numList:", error);
            numList = [];
        }
    }

    // Now numList is guaranteed to be an array
    let calSubTotal = numList.reduce((acc, curr) => acc + (+curr), 0);
    updated.value.subTotal = calSubTotal;

    return updated.value.subTotal;

}

const discount = () => {
    const customerValue = updated.value.customerType
    let discountPercent = 0
    if (customerValue === 'Member') {
        if (subTotal() >= 5000 && subTotal() < 10000) {
            discountPercent = 5
        } else if (subTotal() >= 10000 && subTotal() < 20000) {
            discountPercent = 10
        } else if (subTotal() >= 20000 && subTotal() < 30000) {
            discountPercent = 20
        } else if (subTotal() >= 30000) {
            discountPercent = 30
        }
    }
    updated.value.discount = discountPercent
    return discountPercent
}

const calDiscount = () => {
    const discountAmount = subTotal() * (discount() / 100)
    return discountAmount
}

const total = () => {
    let calTotal = Number(subTotal() - calDiscount())
    updated.value.total = calTotal
    return updated.value.total
}

const customerType = (type) => {
    if (type === 'Guest') {
        updated.value.customerType = 'Guest'
    }
    if (type === "Member") {
        updated.value.customerType = "Member"
    }
}

const minus = (discount) => {
    if (discount == 0) {
        return discount
    }
    else {
        return '-' + discount
    }
}

const comma = (num) => {
    return num.toLocaleString("en")
}


const calculateSubTotalAndSendToBackend = async () => {

    const sendData = {
        numList: updated.value.numList,
        customerType: updated.value.customerType,
        discount: discount(),
        total: total()
    };

    sendData.numList = JSON.stringify(sendData.numList);

    console.log(sendData);

    try {
        const res = await fetch('http://localhost:8080/api/history', {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(sendData)
        });
        if (res.status === 201) {
            // Data sent successfully
            console.log('Add successfully');
            // Reset the data after sending
            updated.value = {
                numList: [],
                dateTime: "",
                customerType: "Guest",
                subTotal: 0,
                discount: 0,
                total: 0,
            };
            addedNumList.value = []; // เคลียร์ addedNumList ให้ว่าง
        } else {
            throw Error('Oops, sorry cannot add');
        }
    } catch (err) {
        console.error(err);
    }
};


const editingIndex = ref(-1);

const startEdit = (index) => {
    editingIndex.value = index;
}

const finishEdit = () => {
    editingIndex.value = -1;
};

</script>
 
<template>
    <!-- Cashier -->
    <div class="Title ">
        <div class="mx-40">
            Cashier Calculator
        </div>
    </div>

    <!-- input & show -->
    <div class="CalculateBorder">
        <div class="flex flex-col">

            <div class="flex flex-row mt-7 justify-center">
                <input v-model="numberInput" type="number" class="w-10/12 h-10 rounded-lg mr-5 text-black"
                    style="background-color: #FFFFFF;" min="0" @input="numberInput = Math.max($event.target.value, 0)">
                <button @click='addNumList()' class="Button">Add</button>
            </div>

            <div class="w-11/12 h-48 mx-auto mt-6 bg-white rounded-lg text-black overflow-auto">
                <div class="ml-10 mt-6 font-bold" v-if="(updated.numList || []).length > 0"> No </div>
                <div class="ml-10 mr-10 mb-4">
                    <div class="flex flex-row items-center mb-1 mt-4" v-for="(number, index) in updated.numList"
                        :key="index">

                        <div v-if="editingIndex === index" class="flex items-center w-full">
                            <input v-model="updated.numList[editingIndex]" type="number"
                                class="w-full h-6 rounded-lg mr-5 text-black" style="background-color: #e0e0e0;" min="0"
                                @input="updated.numList[editingIndex] = Math.max($event.target.value, 0)">
                            <button @click="finishEdit()"
                                class="w-10 h-6 rounded-lg flex items-center justify-center ml-auto"
                                style="background-color: #4263EB; color: #FFFFFF; float: right;">Save</button>
                        </div>
                        <div v-else class="flex items-center">
                            {{ comma(index + 1) }}. {{ comma(number) }} ฿
                        </div>
                        <hr class="w-12/12">
                        <button v-if="editingIndex !== index" @click="startEdit(index)"
                            class="flex items-center justify-end ml-auto">
                            <editIcon /> &nbsp; &nbsp;
                        </button>
                        <button v-if="editingIndex !== index" class="flex items-center" @click="deleteItemList(index)">
                            <trash /> &nbsp; &nbsp;
                        </button>
                        <button v-if="editingIndex === index && editingIndex !== index" @click="finishEdit()"
                            class="w-10 h-6 rounded-lg flex items-center justify-center"
                            style="background-color: #4263EB; color: #FFFFFF">Edit</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="flex flex-row mt-3 ml-14">
            <div class="text-black">Customer:</div>
            <button class="w-6 h-6 rounded-full border-2 border-white bg-white ml-4 focus:outline-none"
                :class="{ 'bg-orange-400': updated.customerType === 'Guest' }" @click="customerType('Guest')"></button>
            <div class="text-black ml-1">Guest</div>

            <button class="w-6 h-6 rounded-full border-2 border-white bg-white ml-4 focus:outline-none"
                :class="{ 'bg-orange-400': updated.customerType === 'Member' }" @click="customerType('Member')"></button>
            <div class="text-black ml-1">Member</div>
        </div>

        <div class="flex flex-col">
            <div class="text-black ml-14 mt-3"> Discount ({{ updated.customerType }}) :</div>
            <div class="flex flex-row mt-3 ml-14">
                <div class="w-20 h-11 flex justify-center items-center rounded-lg text-lg mr-2" :class="getPercentBox(1)">5%
                </div>
                <div class="w-20 h-11 flex justify-center items-center rounded-lg text-lg mr-2" :class="getPercentBox(2)">
                    10%</div>
                <div class="w-20 h-11 flex justify-center items-center rounded-lg text-lg mr-2" :class="getPercentBox(3)">
                    20%</div>
                <div class="w-20 h-11 flex justify-center items-center rounded-lg text-lg " :class="getPercentBox(4)">30%
                </div>
            </div>
        </div>

        <div>
            <div class="flex flex-row justify-end mr-14 mt-5 font-medium text-gray-500">
                <div class="w-auto flex justify-end items-center ">Sub Total : {{ comma(subTotal()) }} ฿</div>
            </div>

            <div class="flex flex-row justify-end mr-14 font-medium text-red-600">
                <div class="w-auto  flex justify-center items-center ">Discount : {{ minus(comma(calDiscount())) }} ฿ </div>
            </div>

            <div class="flex flex-row justify-end mr-14 font-bold text-xl text-green-700">
                <div class="w-auto flex justify-center items-center ">Total : {{ comma(total()) }} ฿</div>
            </div>
        </div>

        <div class="flex justify-end my-3">
            <button class="w-20 h-10 flex justify-center items-center bg-red-600 rounded-lg text-sm text-white mb-2 mr-14"
                v-if="updated.id" @click="$emit('edit', updated)">
                Confirm
            </button>

            <button class="Button" @click="calculateSubTotalAndSendToBackend(updated)" v-else>
                Confirm
            </button>
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
    /* 4rem เทียบเท่ากับ h-16 */
    display: flex;
    align-items: center;
    font-size: 1.5rem;
    /* 2rem เทียบเท่ากับ text-2xl */
    font-weight: 700;
    /* 500 เทียบเท่ากับ font-medium */
    color: #ffffff;
}

.CalculateBorder {
    width: 80%;
    height: 80%;
    margin: 0 auto;
    border-radius: 1.5rem;
    background-color: rgba(222, 222, 222, 0.6);
    /* กำหนดสีพื้นหลังโปร่งใส */
    box-shadow: 0px 10px 15px rgba(0, 0, 0, 0.15);
    backdrop-filter: blur(10px);
    /* ใช้ backdrop-filter เพื่อให้มี Glass Effect */
}

.Button {
    width: 5rem;
    /* w-20 เทียบเท่ากับ 5rem */
    height: 2.5rem;
    /* h-10 เทียบเท่ากับ 2.5rem */
    display: flex;
    justify-content: center;
    /* justify-center เทียบเท่ากับ justify-content: center; */
    align-items: center;
    /* items-center เทียบเท่ากับ align-items: center; */
    border-radius: 0.5rem;
    /* rounded-lg เทียบเท่ากับ border-radius: 0.5rem; */
    font-size: 1rem;
    /* text-sm เทียบเท่ากับ font-size: 1rem; */
    color: #fff;
    /* text-white เทียบเท่ากับ color: #fff; */
    margin-bottom: 0.5rem;
    /* mb-2 เทียบเท่ากับ margin-bottom: 0.5rem; */
    margin-right: 3.5rem;
    /* mr-14 เทียบเท่ากับ margin-right: 3.5rem; */
    background-color: #cc7648;
}
</style>