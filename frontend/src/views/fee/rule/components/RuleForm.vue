<template>
  <el-form
    ref="formRef"
    :model="form"
    :rules="rules"
    label-width="100px"
  >
    <el-form-item label="规则名称" prop="name">
      <el-input v-model="form.name" placeholder="请输入规则名称" />
    </el-form-item>
    
    <el-form-item label="规则编码" prop="code">
      <el-input v-model="form.code" placeholder="请输入规则编码" />
    </el-form-item>
    
    <el-form-item label="计费类型" prop="type">
      <el-select v-model="form.type" placeholder="请选择计费类型">
        <el-option label="固定金额" :value="1" />
        <el-option label="面积计费" :value="2" />
        <el-option label="用量计费" :value="3" />
      </el-select>
    </el-form-item>
    
    <el-form-item label="计费周期" prop="period">
      <el-select v-model="form.period" placeholder="请选择计费周期">
        <el-option label="一次性" :value="1" />
        <el-option label="月付" :value="2" />
        <el-option label="季付" :value="3" />
        <el-option label="年付" :value="4" />
      </el-select>
    </el-form-item>
    
    <!-- 固定金额配置 -->
    <template v-if="form.type === 1">
      <el-form-item label="基础费用" prop="config.baseAmount">
        <el-input-number
          v-model="form.config.baseAmount"
          :precision="2"
          :step="0.01"
          :min="0"
        />
      </el-form-item>
    </template>
    
    <!-- 面积计费/用量计费配置 -->
    <template v-if="form.type === 2 || form.type === 3">
      <el-form-item label="计费方式">
        <el-radio-group v-model="pricingMode">
          <el-radio :label="1">统一单价</el-radio>
          <el-radio :label="2">阶梯计价</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <!-- 统一单价 -->
      <el-form-item
        v-if="pricingMode === 1"
        :label="form.type === 2 ? '单价(元/㎡)' : '单价(元/度)'"
        prop="config.unitPrice"
      >
        <el-input-number
          v-model="form.config.unitPrice"
          :precision="2"
          :step="0.01"
          :min="0"
        />
      </el-form-item>
      
      <!-- 阶梯计价 -->
      <template v-if="pricingMode === 2">
        <div class="tier-rules">
          <div class="tier-rules__header">
            <span>阶梯计价规则</span>
            <el-button
              type="primary"
              link
              @click="addTier"
            >添加阶梯</el-button>
          </div>
          
          <div
            v-for="(tier, index) in form.config.tiers"
            :key="index"
            class="tier-rule"
          >
            <el-input-number
              v-model="tier.start"
              :precision="2"
              :step="1"
              :min="0"
              placeholder="起始值"
            />
            <span class="tier-rule__separator">-</span>
            <el-input-number
              v-model="tier.end"
              :precision="2"
              :step="1"
              :min="0"
              placeholder="结束值"
            />
            <el-input-number
              v-model="tier.price"
              :precision="2"
              :step="0.01"
              :min="0"
              placeholder="单价"
            />
            <el-button
              type="danger"
              link
              @click="removeTier(index)"
            >删除</el-button>
          </div>
        </div>
      </template>
    </template>
  </el-form>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['update:modelValue'])

// 表单数据
const form = reactive({
  ...props.modelValue,
  config: {
    baseAmount: 0,
    unitPrice: 0,
    tiers: []
  }
})

// 计费方式：1-统一单价，2-阶梯计价
const pricingMode = ref(1)

// 监听表单数据变化
watch(
  () => form,
  (val) => {
    emit('update:modelValue', val)
  },
  { deep: true }
)

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入规则编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择计费类型', trigger: 'change' }],
  period: [{ required: true, message: '请选择计费周期', trigger: 'change' }],
  'config.baseAmount': [{ required: true, message: '请输入基础费用', trigger: 'blur' }],
  'config.unitPrice': [{ required: true, message: '请输入单价', trigger: 'blur' }]
}

// 添加阶梯
const addTier = () => {
  if (!form.config.tiers) {
    form.config.tiers = []
  }
  form.config.tiers.push({
    start: 0,
    end: 0,
    price: 0
  })
}

// 删除阶梯
const removeTier = (index) => {
  form.config.tiers.splice(index, 1)
}

// 表单引用
const formRef = ref()

// 暴露方法
defineExpose({
  validate: () => formRef.value.validate()
})
</script>

<style scoped>
.tier-rules {
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  padding: 16px;
  margin-bottom: 20px;
}

.tier-rules__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.tier-rule {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.tier-rule__separator {
  margin: 0 8px;
  color: var(--el-text-color-secondary);
}
</style>