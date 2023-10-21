<script setup>
import NavBar from './component/Nav.vue';
import { RouterView } from 'vue-router';
</script>

<template>
    <div class="Background w-full h-screen">

        <div class="NavBarStyle">
            <div class="flex flex-row w-full h-full">

                <div class="FreshMart">FreshMart
                    <img src="./component/icon/orange.png" class="FreshMartIcon">
                </div>

                <NavBar class= "ContentInNavbar space-x-16"/>
            </div>
        </div>
        <RouterView></RouterView>

    </div>
</template>

<style scoped>
*{
    font-family: 'Kanit', sans-serif;

}
.NavBarStyle{
    width: 100%;
    height: 3.5rem;
    background-color: rgba(255, 255, 255, 0.6); /* กำหนดสีพื้นหลังโปร่งใส */
    backdrop-filter: blur(10px); /* ใช้ backdrop-filter เพื่อให้มี Glass Effect */
    box-shadow: 0px 7px 10px rgba(0, 0, 0, 0.1);

    color: #cc7648;
}
.FreshMart{
    display: flex;
    align-items: center;
    justify-content: center;
    width: 250px;
    height: 100%;
    font-weight: 700;
    font-size: 35px;
    padding: 0 30px 0px 30px;
}
.FreshMartIcon{
    width: 40px;
    display: flex;
    padding: 5px;
}
.ContentInNavbar {

  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.125rem;
  font-weight: 600;
  margin-left: 40px;
}
.Background{
    background-image: url(../src/component/wallpaper/5892969.jpg);
    background-size: cover;
}
</style>