<template>
<div >
  <h2>{{ title }}</h2>
    <pie-chart
      class="pieChart"
      id="set-actual-pie-chart"
      legend="bottom"
      width="230px" height="230px"
      donut="true"
      :round="2"
      :colors="['#004587', '#358fe6']"
      :data="setActualWorkingPieChartData"
      >
    </pie-chart>
</div>
</template>

<script>
import setActualWorkingPieApi from '../mixins/SetActualWorkingPieApi';

export default {
  name: 'SetActualComparison',
  mixins: [setActualWorkingPieApi],
  data() {
    return {
      title: 'Soll/Ist-Stundenvergleich',
    }
  },
  mounted() {
    this.fetchSetActualWorkingPieChartData();
  }
}
</script>
<style>
  .pieChart{
    position: fixed;
    top: 60%;
    left: 77%;
  }
</style>
