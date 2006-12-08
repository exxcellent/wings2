package org.wings.header;

import org.wings.SFrame;
import org.wings.session.SessionManager;
import org.wings.util.SessionLocal;

import java.util.*;

public class Headers
    extends SessionLocal
    implements List
{
    public static final Headers INSTANCE = new Headers();

    private Headers() {
    }

//    protected Object initialValue() {
//        return new ArrayList();
//    }
//
//    List getList() {
//        return (List)get();
//    }
//
//    public int size() {
//        return getList().size();
//    }
//
//    public boolean isEmpty() {
//        return getList().isEmpty();
//    }
//
//    public boolean contains(Object o) {
//        return getList().contains(o);
//    }
//
//    public Iterator iterator() {
//        return getList().iterator();
//    }
//
//    public Object[] toArray() {
//        return getList().toArray();
//    }
//
//    public Object[] toArray(Object[] a) {
//        return getList().toArray(a);
//    }
//
//    public boolean add(Object o) {
//        return getList().add(o);
//    }
//
//    public boolean remove(Object o) {
//        return getList().remove(o);
//    }
//
//    public boolean containsAll(Collection c) {
//        return getList().containsAll(c);
//    }
//
//    public boolean addAll(Collection c) {
//        return getList().addAll(c);
//    }
//
//    public boolean addAll(int index, Collection c) {
//        return getList().addAll(index, c);
//    }
//
//    public boolean removeAll(Collection c) {
//        return getList().removeAll(c);
//    }
//
//    public boolean retainAll(Collection c) {
//        return getList().retainAll(c);
//    }
//
//    public void clear() {
//        getList().clear();
//    }
//
//    public boolean equals(Object o) {
//        return getList().equals(o);
//    }
//
//    public int hashCode() {
//        return getList().hashCode();
//    }
//
//    public Object get(int index) {
//        return getList().get(index);
//    }
//
//    public Object set(int index, Object element) {
//        return getList().set(index, element);
//    }
//
//    public void add(int index, Object element) {
//        getList().add(index, element);
//    }
//
//    public Object remove(int index) {
//        return getList().remove(index);
//    }
//
//    public int indexOf(Object o) {
//        return getList().indexOf(o);
//    }
//
//    public int lastIndexOf(Object o) {
//        return getList().lastIndexOf(o);
//    }
//
//    public ListIterator listIterator() {
//        return getList().listIterator();
//    }
//
//    public ListIterator listIterator(int index) {
//        return getList().listIterator(index);
//    }
//
//    public List subList(int fromIndex, int toIndex) {
//        return getList().subList(fromIndex, toIndex);
//    }
//}

// ----

    protected Object initialValue() {
        return new ArrayList();
    }

    List getList() {
        return (List)get();
    }

    public int size() {
        return getList().size();
    }

    public boolean isEmpty() {
        return getList().isEmpty();
    }

    public boolean contains(Object o) {
        return getList().contains(o);
    }

    public Iterator iterator() {
        return getList().iterator();
    }

    public Object[] toArray() {
        return getList().toArray();
    }

    public Object[] toArray(Object[] a) {
        return getList().toArray(a);
    }

    public boolean add(Object o) {
        boolean changed = getList().add(o);
        if (changed) invalidateFrames();
        return changed;
    }

    public boolean remove(Object o) {
        boolean changed = getList().remove(o);
        if (changed) invalidateFrames();
        return changed;
    }

    public boolean containsAll(Collection c) {
        return getList().containsAll(c);
    }

    public boolean addAll(Collection c) {
        boolean changed = getList().addAll(c);
        if (changed) invalidateFrames();
        return changed;
    }

    public boolean addAll(int index, Collection c) {
        boolean changed = getList().addAll(index, c);
        if (changed) invalidateFrames();
        return changed;
    }

    public boolean removeAll(Collection c) {
        boolean changed = getList().removeAll(c);
        if (changed) invalidateFrames();
        return changed;
    }

    public boolean retainAll(Collection c) {
        boolean changed = getList().retainAll(c);
        if (changed) invalidateFrames();
        return changed;
    }

    public void clear() {
        if (!isEmpty()) invalidateFrames();
        getList().clear();
    }

    public boolean equals(Object o) {
        return getList().equals(o);
    }

    public int hashCode() {
        return getList().hashCode();
    }

    public Object get(int index) {
        return getList().get(index);
    }

    public Object set(int index, Object element) {
        invalidateFrames();
        return getList().set(index, element);
    }

    public void add(int index, Object element) {
        invalidateFrames();
        getList().add(index, element);
    }

    public Object remove(int index) {
        invalidateFrames();
        return getList().remove(index);
    }

    public int indexOf(Object o) {
        return getList().indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return getList().lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return getList().listIterator();
    }

    public ListIterator listIterator(int index) {
        return getList().listIterator(index);
    }

    public List subList(int fromIndex, int toIndex) {
        return getList().subList(fromIndex, toIndex);
    }

    private void invalidateFrames() {
        Set frames = SessionManager.getSession().getFrames();
        for (Iterator it = frames.iterator(); it.hasNext();) {
            ((SFrame) it.next()).reload();
        }
    }
}
