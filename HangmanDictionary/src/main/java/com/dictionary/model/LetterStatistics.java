package com.dictionary.model;

import java.util.ArrayList;
import java.util.List;

public class LetterStatistics {
    private int count = 0;
    private List<Integer> positions = new ArrayList<Integer>();
    
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Integer> getPositions() {
		return positions;
	}
	public void setPositions(List<Integer> positions) {
		this.positions = positions;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((positions == null) ? 0 : positions.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetterStatistics other = (LetterStatistics) obj;
		if (count != other.count)
			return false;
		if (positions == null) {
			if (other.positions != null)
				return false;
		} else if (!positions.equals(other.positions))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "LetterStatistics [count=" + count + ", positions=" + positions + "]";
	}
    
}
