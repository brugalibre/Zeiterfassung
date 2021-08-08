<template>
<div>
    <h2>{{ title }}</h2>
    <p></p>
    <pie-chart
    id="set-actual-pie-chart"
      legend="bottom"
      width="250px" height="250px"
      donut="true"
      :precision="1"
      :colors="['#004587', '#358fe6']"
      :data="setActualWorkingPieChartData"
      >
    </pie-chart>
</div>
</template>

<script>
import setActualWorkingHoursApi from '../mixins/SetActualWorkingHoursApi';

export default {
  name: 'SetActualComparison',
  mixins: [setActualWorkingHoursApi],
  data() {
    return {
      title: 'Soll/Ist-Stundenvergleich',
    }
  },
  mounted() {
    this.fetchSetActualWorkingHoursDto();
  }
}
</script>
