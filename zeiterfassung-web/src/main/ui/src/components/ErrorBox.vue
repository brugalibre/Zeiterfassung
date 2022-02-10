<template>
  <div class="error-box">
    <strong>Fehler!</strong>
    <div>
      <slot></slot>
    </div>
  </div>
</template>

<style>
.error-box {
  padding: 3px;
  background-color: #ffcccb;
  border-color: black;
  border-style: solid;
  border-radius: 5px;
}
</style>
