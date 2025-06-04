<template>
  <div class="rule-tester">
    <div class="rule-tester__header">
      <h3>规则测试</h3>
      <el-button type="primary" @click="handleTest">测试计算</el-button>
    </div>
    
    <el-form
      ref="formRef"
      :model="form"
      label-width="120px"
      class="rule-tester__form"
    >
      <el-form-item
        :label="rule.type === 2 ? '房屋面积(㎡)' : '用量数值'"
        prop="value"
      >
        <el-input-number
          v-model="form.value"
          :precision="2"
          :step="1"
          :min="0"
          style="width: 180px"
        />
      </el-form-item>
      
      <el-form-item label="计费周期" prop="months">
        <el-input-number
          v-model="form.months"
          :precision="0"
          :step="1"
          :min="1"
          style="width: 180px"
        />
        <span class="form-item-help">月</span>
      </el-form-item>
    </el-form>
    
    <!-- 测试结果 -->
    <div v-if="testResult" class="rule-tester__result">
      <el-descriptions title="计算结果" :column="1" border>
        <el-descriptions-item label="基础计算">
          <div class="calc-detail">
            <template v-if="rule.type === 1">
              基础费用：{{ formatAmount(rule.config.baseAmount) }} 元
            </template>
            
            <template v-else>
              <template v-if="rule.config.tiers && rule.config.tiers.length > 0">
                <div v-for="(tier, index) in rule.config.tiers" :key="index">
                  {{ formatTierRange(tier) }}：
                  {{ formatAmount(getTierAmount(tier, index)) }} 元
                </div>
              </template>
              <template v-else>
                {{ form.value }} × {{ formatAmount(rule.config.unitPrice) }}
                = {{ formatAmount(form.value * rule.config.unitPrice) }} 元
              </template>
            </template>
          </div>
        </el-descriptions-item>
        
        <el-descriptions-item label="周期换算">
          <div class="calc-detail">
            {{ formatAmount(testResult.baseAmount) }} ×
            {{ form.months }} {{ getPeriodUnit() }}
            = {{ formatAmount(testResult.totalAmount) }} 元
          </div>
        </el-descriptions-item>
        
        <el-descriptions-item label="最终金额">
          <span class="final-amount">
            {{ formatAmount(testResult.totalAmount) }} 元
          </span>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  rule: {
    type: Object,
    required: true
  }
})

// 表单数据
const form = reactive({
  value: 0,
  months: 1
})

// 测试结果
const testResult = ref(null)

// 获取周期单位
const getPeriodUnit = () => {
  switch (props.rule.period) {
    case 2: return '月'
    case 3: return '季度'
    case 4: return '年'
    default: return ''
  }
}

// 格式化金额
const formatAmount = (amount) => {
  return Number(amount).toFixed(2)
}

// 格式化阶梯范围
const formatTierRange = (tier) => {
  if (tier.end > 0) {
    return `[${tier.start} - ${tier.end}]`
  }
  return `[${tier.start} - ∞]`
}

// 获取阶梯金额
const getTierAmount = (tier, index) => {
  const value = form.value
  let amount = 0
  
  if (value <= tier.start) {
    return 0
  }
  
  if (tier.end > 0 && value > tier.end) {
    amount = (tier.end - tier.start) * tier.price
  } else {
    amount = (value - tier.start) * tier.price
  }
  
  return amount
}

// 测试计算
const handleTest = async () => {
  if (form.value <= 0) {
    ElMessage.warning('请输入有效的计算数值')
    return
  }
  
  // 基础金额计算
  let baseAmount = 0
  if (props.rule.type === 1) {
    baseAmount = props.rule.config.baseAmount
  } else {
    if (props.rule.config.tiers && props.rule.config.tiers.length > 0) {
      // 阶梯计价
      for (const tier of props.rule.config.tiers) {
        baseAmount += getTierAmount(tier)
      }
    } else {
      // 统一单价
      baseAmount = form.value * props.rule.config.unitPrice
    }
  }
  
  // 周期换算
  let totalAmount = baseAmount
  switch (props.rule.period) {
    case 2: // 月付
      totalAmount *= form.months
      break
    case 3: // 季付
      totalAmount *= (form.months / 3)
      break
    case 4: // 年付
      totalAmount *= (form.months / 12)
      break
  }
  
  testResult.value = {
    baseAmount,
    totalAmount
  }
}
</script>

<style scoped>
.rule-tester {
  margin-top: 24px;
  padding: 20px;
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
}

.rule-tester__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.rule-tester__header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}

.rule-tester__form {
  margin-bottom: 20px;
}

.form-item-help {
  margin-left: 8px;
  color: var(--el-text-color-secondary);
}

.rule-tester__result {
  margin-top: 24px;
}

.calc-detail {
  color: var(--el-text-color-regular);
  font-family: monospace;
}

.final-amount {
  color: var(--el-color-danger);
  font-size: 16px;
  font-weight: 500;
}
</style>