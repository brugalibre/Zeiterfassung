<template>
  <h1 class="centeredText"> {{ applicationTitle }} </h1>
</template>

<script>
import appApi from '../mixins/AppApi';

export default {
  name: 'ZeiterfassungDashboard',
  mixins: [appApi],
  data() {
    return {
      applicationTitle: ''
    }
  },
  mounted() {
    this.applicationTitle = this.fetchApplicationTitle();
  }
}
</script>
