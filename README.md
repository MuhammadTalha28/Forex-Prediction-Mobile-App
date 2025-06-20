# 📱 Forex Prediction Mobile App

An Android application that captures candlestick chart screenshots from TradingView and uses a Convolutional Neural Network (CNN) to predict market signals — Buy or Sell — directly on the user's device.

---

## 🚀 Features

- 📷 **Chart Recognition**: Capture or upload candlestick chart images from TradingView.
- 🤖 **ML-Powered Prediction**: Classifies the market sentiment using a trained CNN model.
- ⚡ **On-Device Inference**: No internet needed for predictions — uses TensorFlow Lite.
- 🧠 **Custom-Trained Model**: Trained on labeled chart data with ~82% validation accuracy.
- 📱 **Built with Kotlin**: Smooth native Android experience.

---

## 🧠 Tech Stack

| Layer         | Tools/Frameworks                        |
|---------------|------------------------------------------|
| Language      | Kotlin                                  |
| ML Framework  | TensorFlow / TensorFlow Lite             |
| ML Model      | CNN (Convolutional Neural Network)       |
| Platform      | Android Studio                           |
| Deployment    | Local model inference using TFLite       |

---

## 🛠️ How It Works

1. The user captures a TradingView candlestick chart.
2. The image is preprocessed and passed to a CNN model.
3. The model outputs a Buy/Sell prediction.
4. TensorFlow Lite runs this prediction natively on the device.

---

## 🧪 Model Details

- **Input**: 224x224 RGB candlestick chart images
- **Architecture**: Basic CNN (Conv → ReLU → MaxPool → FC)
- **Accuracy**: 82% on validation data
- **Training Dataset**: Labeled screenshots of market conditions from TradingView

---

## 🧰 Getting Started

### 📦 Requirements

- Android Studio
- Kotlin
- TensorFlow Lite (.tflite model)
- Minimum SDK: 21+

### 🔧 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MuhammadTalha28/Forex-Prediction-Mobile-App.git
