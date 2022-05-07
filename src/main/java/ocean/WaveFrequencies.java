package ocean;

import java.util.Random;

import utils.MathUtils;

public class WaveFrequencies {
	private static final float[] DEFAULT_POWERS = { 0f, 0.051f, 0.122f, 0.201f, 0.245f, 0.324f, 0.417f, 0.519f, 0.619f, 0.647f, 0.928f, 0.993f, 1f, 1f };
	
	private static final float SMALLEST_POW_2 = -4f;
	private static final float MIN_POWER = -6f;
	private static final float MAX_POWER = 5f;

	private int octaveCount;
	private float[] powers;
	
	private float chop;
	private float multiplier;
	
	private float waveDirectionVariance = 90f;
	
	public WaveFrequencies() {
		this(DEFAULT_POWERS, 1f, 1f);
	}
	
	public WaveFrequencies(float[] powerFractions, float chop, float multiplier) {
		this.octaveCount = powerFractions.length;
		this.chop = chop;
		this.multiplier = multiplier;
		
		powers = new float[powerFractions.length];
		for (int i = 0; i < powerFractions.length; i++) {
			powers[i] = MathUtils.lerp(MIN_POWER, MAX_POWER, powerFractions[i]);
		}
	}
	
	public int getOctaveCount() {
		return octaveCount;
	}
	
	public float getChop() {
		return chop;
	}
	
	public void generateWavelengths(int componentsPerOctave, float[] wavelengths, Random prng) {
		float minWavelength = (float) Math.pow(2f, SMALLEST_POW_2);
		
		for (int octave = 0; octave < octaveCount; octave++) {
			for (int component = 0; component < componentsPerOctave; component++) {
				int index = octave * componentsPerOctave + component;
				
				float lesserWavelength = minWavelength + minWavelength / componentsPerOctave * component;
				float greaterWavelength = Math.min(lesserWavelength + minWavelength / componentsPerOctave, 2f * minWavelength);
				wavelengths[index] = MathUtils.lerp(lesserWavelength, greaterWavelength, prng.nextFloat());
			}
			
			minWavelength *= 2f;
		}
	}
	
	public void generateWaveAngles(int componentsPerOctave, float[] angles, Random prng) {
		for (int octave = 0; octave < octaveCount; octave++) {
			for (int component = 0; component < componentsPerOctave; component++) {
				int index = octave * componentsPerOctave + component;
				
				float random = (component + prng.nextFloat()) / componentsPerOctave;
				angles[index] = (2f * random - 1f) * waveDirectionVariance;
			}
		}
	}
	
	public void generateWaveAmlitudes(int componentsPerOctave, float[] amplitudes, float[] wavelengths, float weight, Random prng) {
		for (int index = 0; index < componentsPerOctave * octaveCount; index++) {
			float wavelengthPower = MathUtils.log2(wavelengths[index]);
			wavelengthPower = MathUtils.clamp(wavelengthPower, SMALLEST_POW_2, SMALLEST_POW_2 + octaveCount - 1f);
			
			int powerIndex = (int)(wavelengthPower - SMALLEST_POW_2);
			boolean isLastPower = (powerIndex+1 >= powers.length);
			
			float currentPower = powers[powerIndex];
			float nextPower = !isLastPower ? powers[powerIndex+1] : MIN_POWER;
			
			float lowerWavelength = (float) Math.pow(2f, Math.floor(wavelengthPower));
			float lowerWaveNumber = (float) (2f * Math.PI / lowerWavelength);
			float lowerAngularVelocity = lowerWaveNumber * calculateAngularVelocity(lowerWavelength);
			
			float higherWavelength = 2f * lowerWavelength;
			float higherWaveNumber = (float) (2f * Math.PI / higherWavelength);
			float higherAngularVelocity = higherWaveNumber * calculateAngularVelocity(higherWavelength);

			float deltaAngularVelocity = (lowerAngularVelocity - higherAngularVelocity) / componentsPerOctave;

			float transition = (wavelengths[index] - lowerWavelength) / lowerWavelength;

			float power = !isLastPower ? MathUtils.lerp(currentPower, nextPower, transition) : powers[powerIndex];

			float amplitude = (float) Math.sqrt(2f * Math.pow(10f, power) * deltaAngularVelocity);

	        amplitudes[index] = amplitude * prng.nextFloat() * multiplier * weight;
		}
	}
	
	public static float calculateAngularVelocity(float wavelength) {
        float waveNumber = (float) (2f * Math.PI / wavelength);
        return (float) Math.sqrt(Math.abs(Ocean.GRAVITY) / waveNumber);
	}
}
