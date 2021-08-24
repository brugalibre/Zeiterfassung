<template>
  <div>
    <input
      required
      v-model="timeValueOject.timeRepresentation"
      type="time"
    />
	</div>
</template>
<script>
import dateCalculation from '../mixins/DateCalculation';

export default {
  name: 'TimeInput',
  mixins: [dateCalculation],
  props: ['initTimeRepresentation'],
  emits: ['timeRepChanged'],
  data (){
    return {
      timeValueOject: {
        timeRepresentation: this.initTimeRepresentation,
        timeValue: '',
      }
    }
  },
  methods: {
    onTimeRepChanged: function(){
      this.$emit('timeRepChanged', this.timeValueOject);
    },
    calcTimeValue: function() {
      return this.createDateNowWithTime(this.timeValueOject.timeRepresentation);
    },
  },
  mounted() {
    this.timeValueOject.timeValue = this.calcTimeValue();
  },
  watch: {
    'timeValueOject.timeRepresentation': function(newVal, oldVal){
      console.log("timeValueOject.timeRepresentation: Changed time from old '" + oldVal + ", to new '" + newVal);
      this.timeValueOject.timeValue = this.calcTimeValue();
    },
    'timeValueOject.timeValue': function(newVal, oldVal){
      console.log("timeValueOject.timeValue: Changed time from old '" + oldVal + ", to new '" + newVal);
       this.onTimeRepChanged();
    },
  },
}
</script>
